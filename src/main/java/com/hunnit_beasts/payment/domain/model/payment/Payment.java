package com.hunnit_beasts.payment.domain.model.payment;

import com.hunnit_beasts.payment.domain.model.money.Money;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 결제 애그리거트 루트
 */
@Getter
public class Payment {
    // 식별자
    private final PaymentId id;
    private final ExternalIdentifier identifier;  // 포트원 결제 ID

    // 주문 정보
    private final String orderName;               // 주문명
    private final Money amount;                   // 결제 금액

    // 결제 처리 정보 (값 객체)
    private final PaymentProcessingInfo processingInfo;

    // 결제 상태 정보 (값 객체)
    private final PaymentStatusInfo statusInfo;

    // PG 응답 정보
    private final String pgResponse;              // PG사 응답 (JSON)

    /**
     * 새로운 결제 생성 (팩토리 메서드) - 포트원 데이터 기반
     */
    public static Payment createFromPortone(
            String portonePaymentId,
            String orderName,
            int amount,
            PaymentProcessingInfo processingInfo,
            String pgResponse) {

        return new Payment(
                PaymentId.generate(),
                ExternalIdentifier.of(portonePaymentId),
                orderName,
                Money.won(amount),
                processingInfo,
                new PaymentStatusInfo(PaymentStatus.PENDING, LocalDateTime.now(), null, null),
                pgResponse
        );
    }

    /**
     * 영속성에서 결제 객체 복원 (팩토리 메서드)
     */
    public static Payment reconstitute(
            PaymentId id,
            ExternalIdentifier identifier,
            String orderName,
            Money amount,
            PaymentProcessingInfo processingInfo,
            PaymentStatusInfo statusInfo,
            String pgResponse) {

        return new Payment(
                id,
                identifier,
                orderName,
                amount,
                processingInfo,
                statusInfo,
                pgResponse
        );
    }

    // 생성자 (private)
    private Payment(
            PaymentId id,
            ExternalIdentifier identifier,
            String orderName,
            Money amount,
            PaymentProcessingInfo processingInfo,
            PaymentStatusInfo statusInfo,
            String pgResponse) {

        this.id = Objects.requireNonNull(id, "결제 ID는 null일 수 없습니다");
        this.identifier = Objects.requireNonNull(identifier, "식별자는 null일 수 없습니다");
        this.orderName = Objects.requireNonNull(orderName, "주문명은 null일 수 없습니다");
        this.amount = Objects.requireNonNull(amount, "결제 금액은 null일 수 없습니다");
        this.processingInfo = Objects.requireNonNull(processingInfo, "결제 처리 정보는 null일 수 없습니다");
        this.statusInfo = Objects.requireNonNull(statusInfo, "결제 상태 정보는 null일 수 없습니다");
        this.pgResponse = pgResponse;
    }

    /**
     * 결제 완료 처리 - 새로운 객체 반환 (불변성 유지)
     */
    public Payment complete(String receiptUrl, String updatedPgResponse) {
        if (this.statusInfo.getStatus() == PaymentStatus.SUCCESS)
            return this;

        PaymentStatusInfo updatedStatusInfo = this.statusInfo.complete(receiptUrl);

        return new Payment(
                this.id,
                this.identifier,
                this.orderName,
                this.amount,
                this.processingInfo,
                updatedStatusInfo,
                updatedPgResponse != null ? updatedPgResponse : this.pgResponse
        );
    }

    /**
     * 결제가 완료되었는지 확인
     */
    public boolean isCompleted() {
        return this.statusInfo.getStatus() == PaymentStatus.SUCCESS;
    }

    /**
     * 결제가 취소 가능한지 확인
     */
    public boolean isCancelable() {
        return this.statusInfo.getStatus() == PaymentStatus.SUCCESS;
    }
}