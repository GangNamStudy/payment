package com.hunnit_beasts.payment.domain.exception;

public class PaymentNotCancelableException extends PaymentException {
    public PaymentNotCancelableException(String message) {
        super(message);
    }
}
