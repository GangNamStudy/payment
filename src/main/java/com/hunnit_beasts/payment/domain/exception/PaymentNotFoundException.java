package com.hunnit_beasts.payment.domain.exception;

import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import lombok.Getter;

@Getter
public class PaymentNotFoundException extends PaymentException {
    private final String paymentId;

    public PaymentNotFoundException(String paymentId) {
        super(String.format("결제를 찾을 수 없습니다: %s", paymentId));
        this.paymentId = paymentId;
    }

    public PaymentNotFoundException(PaymentId paymentId) {
        this(paymentId.getValue());
    }

}
