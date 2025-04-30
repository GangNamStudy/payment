package com.hunnit_beasts.payment.domain.model.payment;

import com.hunnit_beasts.payment.domain.event.DomainEvents;
import com.hunnit_beasts.payment.domain.event.payment.PaymentCanceledEvent;
import com.hunnit_beasts.payment.domain.event.payment.PaymentCompletedEvent;
import com.hunnit_beasts.payment.domain.event.payment.PaymentCreatedEvent;
import com.hunnit_beasts.payment.domain.event.payment.PaymentFailedEvent;
import com.hunnit_beasts.payment.domain.exception.InvalidPaymentStateException;
import com.hunnit_beasts.payment.domain.exception.PaymentNotCancelableException;
import com.hunnit_beasts.payment.domain.model.identifier.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.method.PaymentMethod;
import com.hunnit_beasts.payment.domain.model.money.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 결제 애그리거트 루트
 * 결제와 관련된 모든 상태 변경 및 비즈니스 로직을 관리
 */
@Getter
public class Payment {
    // 식별자 및 기본 정보
    private final PaymentId id;
    private final ExternalIdentifier identifier;
    private final OrderInfo orderInfo;
    private final Money amount;
    private final PaymentMethod paymentMethod;

    // 상태 정보
    private PaymentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Receipt receipt;

    // 취소 정보
    private final List<CancelInfo> cancelInfos;

    /**
     * 새로운 결제 생성 (팩토리 메서드)
     */
    public static Payment create(
            ExternalIdentifier identifier,
            OrderInfo orderInfo,
            Money amount,
            PaymentMethod paymentMethod) {

        return new Payment(
                PaymentId.generate(),
                identifier,
                orderInfo,
                amount,
                paymentMethod,
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                null,
                null,
                new ArrayList<>()
        );
    }

    /**
     * 영속성에서 결제 객체 복원 (팩토리 메서드)
     */
    public static Payment reconstitute(
            PaymentId id,
            ExternalIdentifier identifier,
            OrderInfo orderInfo,
            Money amount,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime completedAt,
            Receipt receipt,
            List<CancelInfo> cancelInfos) {

        return new Payment(
                id,
                identifier,
                orderInfo,
                amount,
                paymentMethod,
                status,
                createdAt,
                completedAt,
                receipt,
                new ArrayList<>(cancelInfos)
        );
    }

    // 생성자 (private)
    private Payment(
            PaymentId id,
            ExternalIdentifier identifier,
            OrderInfo orderInfo,
            Money amount,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime completedAt,
            Receipt receipt,
            List<CancelInfo> cancelInfos) {

        this.id = Objects.requireNonNull(id, "결제 ID는 null일 수 없습니다");
        this.identifier = Objects.requireNonNull(identifier, "식별자는 null일 수 없습니다");
        this.orderInfo = Objects.requireNonNull(orderInfo, "주문 정보는 null일 수 없습니다");
        this.amount = Objects.requireNonNull(amount, "결제 금액은 null일 수 없습니다");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "결제 수단은 null일 수 없습니다");
        this.status = Objects.requireNonNull(status, "결제 상태는 null일 수 없습니다");
        this.createdAt = Objects.requireNonNull(createdAt, "생성 시간은 null일 수 없습니다");
        this.completedAt = completedAt;
        this.receipt = receipt;
        this.cancelInfos = Objects.requireNonNull(cancelInfos, "취소 정보 목록은 null일 수 없습니다");

        // 새 결제 생성시 도메인 이벤트 발행
        if (status == PaymentStatus.PENDING)
            DomainEvents.publish(new PaymentCreatedEvent(id, identifier, amount));
    }

    /**
     * 결제 완료 처리
     */
    public void complete(Receipt receipt) {
        validateStateTransition(PaymentStatus.SUCCESS);

        this.status = PaymentStatus.SUCCESS;
        this.completedAt = LocalDateTime.now();
        this.receipt = receipt;

        DomainEvents.publish(new PaymentCompletedEvent(id, identifier, amount));
    }

    /**
     * 결제 실패 처리
     */
    public void fail(String failureReason) {
        validateStateTransition(PaymentStatus.FAILED);

        this.status = PaymentStatus.FAILED;

        DomainEvents.publish(new PaymentFailedEvent(id, identifier, failureReason));
    }

    /**
     * 결제 취소 처리 (전체 금액)
     */
    public void cancel(String reason) {
        cancel(reason, this.amount);
    }

    /**
     * 결제 취소 처리 (부분 또는 전체 금액)
     */
    public void cancel(String reason, Money cancelAmount) {
        if (!isCancelable())
            throw new PaymentNotCancelableException("이 결제는 취소할 수 없습니다: " + this.status);

        // 취소 가능 금액 검증
        Money cancelableAmount = getCancelableAmount();
        if (cancelAmount.isGreaterThan(cancelableAmount))
            throw new IllegalArgumentException(String.format("취소 금액(%s)이 취소 가능 금액(%s)을 초과합니다", cancelAmount, cancelableAmount));

        // 취소 정보 추가
        CancelInfo cancelInfo = CancelInfo.of(reason, cancelAmount);
        this.cancelInfos.add(cancelInfo);

        // 전체 취소인지 부분 취소인지 상태 변경
        if (getRemainingAmount().equals(Money.zero(this.amount.getCurrency()))) {
            validateStateTransition(PaymentStatus.CANCELED);
            this.status = PaymentStatus.CANCELED;
        } else {
            validateStateTransition(PaymentStatus.PARTIALLY_CANCELED);
            this.status = PaymentStatus.PARTIALLY_CANCELED;
        }

        // 도메인 이벤트 발행
        DomainEvents.publish(new PaymentCanceledEvent(id, identifier, cancelAmount, getRemainingAmount(), reason));
    }

    /**
     * 취소 가능 금액 계산
     */
    public Money getCancelableAmount() {
        return getRemainingAmount();
    }

    /**
     * 남은 금액 계산 (전체 금액 - 취소된 금액)
     */
    public Money getRemainingAmount() {
        Money canceledTotal = this.cancelInfos.stream()
                .map(CancelInfo::getAmount)
                .reduce(Money.zero(this.amount.getCurrency()), Money::add);

        return this.amount.subtract(canceledTotal);
    }

    /**
     * 결제 취소 가능 여부 확인
     */
    public boolean isCancelable() {
        return this.status.isCancelable();
    }

    /**
     * 상태 전이 검증
     * 현재 상태에서 새로운 상태로 전이가 가능한지 검증
     */
    private void validateStateTransition(PaymentStatus newStatus) {
        if (!canTransitionTo(newStatus))
            throw new InvalidPaymentStateException(String.format("현재 상태(%s)에서 새로운 상태(%s)로 전환할 수 없습니다", this.status, newStatus));
    }

    /**
     * 현재 상태에서 새로운 상태로 전이 가능한지 확인
     */
    private boolean canTransitionTo(PaymentStatus newStatus) {
        return switch (this.status) {
            case PENDING -> newStatus == PaymentStatus.SUCCESS ||
                    newStatus == PaymentStatus.FAILED;
            case SUCCESS -> newStatus == PaymentStatus.CANCELED ||
                    newStatus == PaymentStatus.PARTIALLY_CANCELED;
            case PARTIALLY_CANCELED -> newStatus == PaymentStatus.CANCELED;
            default -> false;
        };
    }

    public List<CancelInfo> getCancelInfos() {
        return Collections.unmodifiableList(cancelInfos);
    }
}
