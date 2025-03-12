package com.hunnit_beasts.payment.port.in;

import com.hunnit_beasts.payment.application.dto.request.query.PaymentSearchRequestDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import com.hunnit_beasts.payment.domain.vo.PaymentId;

import java.util.Optional;

public interface PaymentQueryUseCase {

    /**
     * 결제 정보 조회
     */
    Optional<PaymentSummaryDto> getPayment(PaymentId paymentId);

    /**
     * 결제 목록 조회
     */
    PaymentListResponseDto getPayments(PaymentSearchRequestDto criteria);
}
