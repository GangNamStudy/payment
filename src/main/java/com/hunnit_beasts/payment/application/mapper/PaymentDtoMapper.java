package com.hunnit_beasts.payment.application.mapper;

import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import org.springframework.stereotype.Component;

/**
 * 결제 도메인 객체와 DTO 간의 변환을 담당하는 매퍼 클래스
 */
@Component
public class PaymentDtoMapper {

    /**
     * 결제 도메인 객체를 응답 DTO로 변환
     */
    public PaymentResponseDto toResponseDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId().getValue())
                .identifier(payment.getIdentifier().getValue())
                .orderName(payment.getOrderName())
                .amount(payment.getAmount().getAmount().intValue())
                .status(payment.getStatusInfo().getStatus().name())
                .paymentDateTime(payment.getStatusInfo().getCreatedAt())
                .receiptUrl(payment.getStatusInfo().getReceiptUrl())
                .paymentMethod(payment.getProcessingInfo() != null ? payment.getProcessingInfo().getPayMethod() : null)
                .build();
    }
}