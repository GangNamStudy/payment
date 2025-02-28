package com.hunnit_beasts.payment.domain.exception;

public class UnsupportedPaymentTypeException extends RuntimeException {
    public UnsupportedPaymentTypeException(String message) {
        super(message);
    }
}
