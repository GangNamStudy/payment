package com.hunnit_beasts.payment.port.in;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.etc.config.annotation.UseCase;

import java.util.concurrent.CompletableFuture;

@UseCase
public interface PaymentCommendUseCase {

    /**
     * 결제 취소 처리
     */
    PaymentCancelResponseDto cancelPayment(String paymentId, PaymentCancelRequestDto request);

    /**
     * 결제 완료 처리
     * @param paymentId 결제 ID
     * @return 결제 처리 결과 (비동기)
     */
    CompletableFuture<PaymentResponseDto> completePayment(String paymentId);
}
