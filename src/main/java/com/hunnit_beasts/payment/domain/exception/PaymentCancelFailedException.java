package com.hunnit_beasts.payment.domain.exception;

public class PaymentCancelFailedException extends RuntimeException {
    public PaymentCancelFailedException(String message) {
        super(message);
    }

    public PaymentCancelFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
