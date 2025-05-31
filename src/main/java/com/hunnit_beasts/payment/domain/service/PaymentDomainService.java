package com.hunnit_beasts.payment.domain.service;

import com.hunnit_beasts.payment.domain.event.DomainEvents;
import com.hunnit_beasts.payment.domain.event.payment.PaymentCompletedEvent;
import com.hunnit_beasts.payment.domain.model.external.ExternalPaymentInfo;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.PaymentProcessingInfo;
import org.springframework.stereotype.Component;

/**
 * 결제 도메인 서비스
 * 복잡한 비즈니스 로직을 캡슐화하고 도메인 규칙을 강제함
 */
@Component
public class PaymentDomainService {

    /**
     * 외부 결제 정보를 바탕으로 결제를 완료 처리
     *
     * @param existingPayment 기존 결제 정보 (null 가능)
     * @param externalIdentifier 외부 식별자
     * @param externalPaymentInfo 외부 결제 정보
     * @return 처리된 결제 정보
     */
    public Payment completePayment(
            Payment existingPayment,
            String externalIdentifier,
            ExternalPaymentInfo externalPaymentInfo) {

        // 결제가 이미 완료된 경우 기존 결제 반환
        if (existingPayment != null && existingPayment.isCompleted()) {
            return existingPayment;
        }

        // 기존 결제가 없으면 새로 생성
        Payment payment = existingPayment != null
                ? existingPayment
                : createNewPayment(externalIdentifier, externalPaymentInfo);

        // 결제 완료 처리
        Payment completedPayment = payment.complete(
                externalPaymentInfo.getReceiptUrl(),
                externalPaymentInfo.getPgResponse()
        );

        // 상태가 변경된 경우에만 이벤트 발행
        if (completedPayment != payment) {
            publishPaymentCompletedEvent(completedPayment, externalIdentifier, externalPaymentInfo);
        }

        return completedPayment;
    }

    /**
     * 새로운 결제 생성
     * 외부 결제 정보를 바탕으로 도메인 객체 생성
     */
    public Payment createNewPayment(String externalIdentifier, ExternalPaymentInfo externalPaymentInfo) {
        validateExternalPaymentInfo(externalPaymentInfo);

        PaymentProcessingInfo processingInfo = PaymentProcessingInfo.of(
                externalPaymentInfo.getTransactionId(),
                externalPaymentInfo.getPgProvider(),
                externalPaymentInfo.getPgTxId(),
                externalPaymentInfo.getPaymentMethod()
        );

        return Payment.createFromPortone(
                externalIdentifier,
                externalPaymentInfo.getOrderName(),
                externalPaymentInfo.getTotalAmount().intValue(),
                processingInfo,
                externalPaymentInfo.getPgResponse()
        );
    }

    /**
     * 결제 완료 이벤트 발행
     */
    private void publishPaymentCompletedEvent(
            Payment completedPayment,
            String externalIdentifier,
            ExternalPaymentInfo externalPaymentInfo) {

        DomainEvents.publish(new PaymentCompletedEvent(
                completedPayment.getId(),
                ExternalIdentifier.of(externalIdentifier),
                Money.won(externalPaymentInfo.getTotalAmount().intValue())
        ));
    }

    /**
     * 외부 결제 정보 유효성 검증
     */
    private void validateExternalPaymentInfo(ExternalPaymentInfo externalPaymentInfo) {
        if (!externalPaymentInfo.isPaymentCompleted()) {
            throw new IllegalStateException("결제가 완료되지 않은 상태입니다");
        }

        if (externalPaymentInfo.getTotalAmount() == null ||
                externalPaymentInfo.getTotalAmount().signum() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 결제 금액입니다");
        }

        if (externalPaymentInfo.getOrderName() == null ||
                externalPaymentInfo.getOrderName().trim().isEmpty()) {
            throw new IllegalArgumentException("주문명이 없습니다");
        }
    }
}