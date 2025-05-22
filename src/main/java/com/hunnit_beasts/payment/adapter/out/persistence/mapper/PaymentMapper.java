package com.hunnit_beasts.payment.adapter.out.persistence.mapper;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.*;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    /**
     * 도메인 객체를 엔티티로 변환
     */
    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .identifier(payment.getIdentifier().getValue())
                .orderName(payment.getOrderName())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getAmount().getCurrency().getCode())
                .transactionId(payment.getProcessingInfo() != null ? payment.getProcessingInfo().getTransactionId() : null)
                .pgProvider(payment.getProcessingInfo() != null ? payment.getProcessingInfo().getPgProvider() : null)
                .pgTxId(payment.getProcessingInfo() != null ? payment.getProcessingInfo().getPgTxId() : null)
                .paymentMethod(payment.getProcessingInfo() != null ? payment.getProcessingInfo().getPayMethod() : "UNKNOWN")
                .status(payment.getStatusInfo().getStatus().name())
                .createdAt(payment.getStatusInfo().getCreatedAt())
                .completedAt(payment.getStatusInfo().getCompletedAt())
                .receiptUrl(payment.getStatusInfo().getReceiptUrl())
                .pgResponse(payment.getPgResponse())
                .build();
    }

    /**
     * 엔티티를 도메인 객체로 변환
     */
    public Payment toDomain(PaymentEntity entity) {
        PaymentId paymentId = PaymentId.of(entity.getId());
        ExternalIdentifier identifier = ExternalIdentifier.of(entity.getIdentifier());
        Money amount = Money.won(entity.getAmount());

        PaymentProcessingInfo processingInfo = PaymentProcessingInfo.of(
                entity.getTransactionId(),
                entity.getPgProvider(),
                entity.getPgTxId(),
                entity.getPaymentMethod()
        );

        PaymentStatus status = PaymentStatus.valueOf(entity.getStatus());

        PaymentStatusInfo statusInfo = PaymentStatusInfo.of(
                status,
                entity.getCreatedAt(),
                entity.getCompletedAt(),
                entity.getReceiptUrl()
        );

        return Payment.reconstitute(
                paymentId,
                identifier,
                entity.getOrderName(),
                amount,
                processingInfo,
                statusInfo,
                entity.getPgResponse()
        );
    }
}