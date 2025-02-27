package com.hunnit_beasts.payment.domain.exception;

public class PaymentNotCancelableException extends RuntimeException {
    public PaymentNotCancelableException(String message) {
        super(message);
    }
}
