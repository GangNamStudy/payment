package com.hunnit_beasts.payment.domain.service;

import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.Receipt;

/**
 * 결제 도메인 서비스
 * 여러 애그리거트에 걸친 복잡한 도메인 로직 처리
 */
public class PaymentDomainService {

    /**
     * 결제 유효성 검증
     */
    public void validatePayment(Payment payment) {
        // 결제 데이터 유효성 검증
        if (payment.getAmount().getAmount().doubleValue() <= 0)
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다");

        // 결제 수단 유효성 검증
        payment.getPaymentMethod().validate();
    }

    /**
     * 결제가 완료된 시점에서 필요한 도메인 로직 수행
     */
    public void completePayment(Payment payment, Receipt receipt) {
        payment.complete(receipt);
    }

    /**
     * 결제가 실패된 시점에서 필요한 도메인 로직 수행
     */
    public void failPayment(Payment payment, String failureReason) {
        payment.fail(failureReason);
    }

    /**
     * 부분 결제 취소 처리
     */
    public void cancelPayment(Payment payment, String reason, Money cancelAmount) {
        payment.cancel(reason, cancelAmount);
    }

    /**
     * 전체 결제 취소 처리
     */
    public void cancelPayment(Payment payment, String reason) {
        payment.cancel(reason);
    }
}
