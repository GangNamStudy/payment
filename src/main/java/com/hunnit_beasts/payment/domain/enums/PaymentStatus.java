package com.hunnit_beasts.payment.domain.enums;

public enum PaymentStatus {
    SUCCESS, FAILED, PENDING, CANCELED;

    public boolean isCancellable() {
        return this == SUCCESS;
    }
}
