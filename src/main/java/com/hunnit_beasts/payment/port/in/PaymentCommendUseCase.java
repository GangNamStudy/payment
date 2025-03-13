package com.hunnit_beasts.payment.port.in;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.request.commend.PaymentRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.domain.vo.PaymentId;

public interface PaymentCommendUseCase {

    /**
     * 결제 요청 처리
     */
    PaymentResponseDto processPayment(PaymentRequestDto request);

    /**
     * 결제 취소 처리
     */
    PaymentCancelResponseDto cancelPayment(PaymentId paymentId, PaymentCancelRequestDto request);
}
