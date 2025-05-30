package com.hunnit_beasts.payment.port.in;

import com.hunnit_beasts.payment.etc.config.annotation.UseCase;

import java.util.concurrent.CompletableFuture;

@UseCase
public interface PaymentWebhookUseCase {

    /**
     * 웹훅을 통한 결제 정보 업데이트
     * @param impUid 웹훅 ID
     * @param merchantUid 웹훅 타임스탬프
     * @param status 웹훅 서명
     * @return 처리 결과 (비동기)
     */
    CompletableFuture<Void> handleWebhook(
            String impUid,
            String merchantUid,
            String status);
}