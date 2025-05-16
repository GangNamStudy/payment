package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 결제 처리 정보 값 객체
 */
@Getter
@ToString
@EqualsAndHashCode
public final class PaymentProcessingInfo {
    private final String transactionId;     // 트랜잭션 ID
    private final String pgProvider;        // PG사 제공자
    private final String pgTxId;            // PG사 트랜잭션 ID
    private final String payMethod;         // 결제 수단

    public PaymentProcessingInfo(
            String transactionId,
            String pgProvider,
            String pgTxId,
            String payMethod) {

        this.transactionId = transactionId;
        this.pgProvider = pgProvider;
        this.pgTxId = pgTxId;
        this.payMethod = payMethod;
    }

    /**
     * 새로운 인스턴스 생성 (팩토리 메서드)
     */
    public static PaymentProcessingInfo of(
            String transactionId,
            String pgProvider,
            String pgTxId,
            String payMethod) {

        return new PaymentProcessingInfo(
                transactionId,
                pgProvider,
                pgTxId,
                payMethod
        );
    }
}