package com.hunnit_beasts.payment.port.out;

import java.util.concurrent.CompletableFuture;

/**
 * 결제 시스템 클라이언트 인터페이스
 */
public interface PaymentClient {

    /**
     * 결제 정보 조회
     * @param paymentId 결제 ID
     * @return 결제 정보 (비동기)
     */
    CompletableFuture<?> getPayment(String paymentId);
}