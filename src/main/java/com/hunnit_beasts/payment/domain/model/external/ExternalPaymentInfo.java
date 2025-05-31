package com.hunnit_beasts.payment.domain.model.external;

import java.math.BigDecimal;

/**
 * 외부 결제 시스템의 정보를 추상화한 인터페이스
 */
public interface ExternalPaymentInfo {

    /**
     * 트랜잭션 ID 반환
     */
    String getTransactionId();

    /**
     * 주문명 반환
     */
    String getOrderName();

    /**
     * 결제 금액 반환
     */
    BigDecimal getTotalAmount();

    /**
     * 영수증 URL 반환
     */
    String getReceiptUrl();

    /**
     * PG 제공자 반환
     */
    String getPgProvider();

    /**
     * PG 트랜잭션 ID 반환
     */
    String getPgTxId();

    /**
     * 결제 수단 반환
     */
    String getPaymentMethod();

    /**
     * PG 응답 정보 반환 (JSON)
     */
    String getPgResponse();

    /**
     * 결제가 성공적으로 완료되었는지 확인
     */
    boolean isPaymentCompleted();
}