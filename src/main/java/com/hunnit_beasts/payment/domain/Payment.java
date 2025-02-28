package com.hunnit_beasts.payment.domain;

import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.event.*;
import com.hunnit_beasts.payment.domain.vo.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 결제(Payment) 애그리거트 루트 엔티티
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    private PaymentId id;                 // 결제 고유 식별자(값 객체로 변경)
    private String identifier;            // 외부 시스템 식별자
    private String orderName;             // 주문명
    private Money amount;                 // 결제 금액
    private PaymentType paymentType;      // 결제 유형(전략 패턴 적용)
    private PaymentStatus status;         // 결제 상태
    private LocalDateTime paymentDateTime; // 결제 시간
    private String receiptUrl;            // 영수증 URL
    private PaymentInfo paymentInfo;      // 결제 정보

    // 부분 취소 지원을 위해 리스트로 변경
    private final List<CancelInfo> cancelInfos = new ArrayList<>();

    // 도메인 이벤트 추가
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    @Builder
    public Payment(String identifier, String orderName, Money amount,
                   PaymentType paymentType, PaymentInfo paymentInfo) {
        this.id = new PaymentId();  // 식별자 자동 생성
        this.identifier = Objects.requireNonNull(identifier, "식별자는 필수입니다");
        this.orderName = Objects.requireNonNull(orderName, "주문명은 필수입니다");
        this.amount = Objects.requireNonNull(amount, "금액은 필수입니다");
        this.paymentType = Objects.requireNonNull(paymentType, "결제 유형은 필수입니다");
        this.paymentInfo = Objects.requireNonNull(paymentInfo, "결제 정보는 필수입니다");
        this.status = PaymentStatus.PENDING;
        this.paymentDateTime = LocalDateTime.now();

        // 결제 생성 이벤트 발행
        registerEvent(new PaymentCreatedEvent(this.id, this.identifier, this.amount));
    }

    /**
     * 결제 성공 처리
     */
    public void completePayment(String receiptUrl) {
        // 상태 기계 패턴 적용 - 유효한 상태 전이 검증
        PaymentStatusTransition.validate(this.status, PaymentStatus.SUCCESS);

        this.status = PaymentStatus.SUCCESS;
        this.receiptUrl = receiptUrl;

        // 결제 완료 이벤트 발행
        registerEvent(new PaymentCompletedEvent(this.id, this.identifier, this.amount));
    }

    /**
     * 결제 실패 처리
     */
    public void failPayment(String failureReason) {
        PaymentStatusTransition.validate(this.status, PaymentStatus.FAILED);

        this.status = PaymentStatus.FAILED;

        // 결제 실패 이벤트 발행
        registerEvent(new PaymentFailedEvent(this.id, this.identifier, failureReason));
    }

    /**
     * 결제 전체 취소 처리
     */
    public void cancelPayment(String reason) {
        cancelPayment(reason, this.amount);
    }

    /**
     * 결제 부분/전체 취소 처리
     */
    public void cancelPayment(String reason, Money cancelAmount) {
        PaymentStatusTransition.validate(this.status, PaymentStatus.CANCELED);

        // 취소 가능 금액 계산
        Money cancelableAmount = getCancelableAmount();
        if (cancelAmount.isGreaterThan(cancelableAmount))
            throw new IllegalArgumentException(
                    String.format("취소 금액(%s)이 취소 가능 금액(%s)을 초과합니다",
                            cancelAmount, cancelableAmount));

        // 취소 정보 추가
        CancelInfo cancelInfo = new CancelInfo(reason, cancelAmount, LocalDateTime.now());
        this.cancelInfos.add(cancelInfo);

        // 전체 취소인 경우 상태 변경
        if (getRemainingAmount().equals(Money.zero(this.amount.getCurrency())))
            this.status = PaymentStatus.CANCELED;
        else
            this.status = PaymentStatus.PARTIALLY_CANCELED;

        // 결제 취소 이벤트 발행
        registerEvent(new PaymentCanceledEvent(this.id, this.identifier, cancelAmount, getRemainingAmount()));
    }

    /**
     * 취소 가능한 금액 계산
     */
    public Money getCancelableAmount() {
        return getRemainingAmount();
    }

    /**
     * 남은 금액 계산
     */
    public Money getRemainingAmount() {
        Money canceledTotal = this.cancelInfos.stream()
                .map(CancelInfo::getCancelAmount)
                .reduce(Money.zero(this.amount.getCurrency()), Money::add);

        return this.amount.subtract(canceledTotal);
    }

    /**
     * 결제가 취소 가능한지 확인
     */
    public boolean isCancelable() {
        return this.status == PaymentStatus.SUCCESS ||
                this.status == PaymentStatus.PARTIALLY_CANCELED;
    }

    /**
     * 결제가 환불 가능한지 확인
     */
    public boolean isRefundable() {
        // 취소된 결제도 환불 가능
        return this.status == PaymentStatus.SUCCESS ||
                this.status == PaymentStatus.PARTIALLY_CANCELED ||
                this.status == PaymentStatus.CANCELED;
    }

    /**
     * 도메인 이벤트 등록
     */
    private void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * 발행된 모든 도메인 이벤트 조회 후 초기화
     */
    public List<DomainEvent> flushEvents() {
        List<DomainEvent> events = Collections.unmodifiableList(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
}