package com.hunnit_beasts.payment.domain.model.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("결제 진행 중", false, false),
    AUTHORIZED("결제 승인됨", false, false),
    SUCCESS("결제 성공", true, true),
    FAILED("결제 실패", false, false),
    PARTIALLY_CANCELED("부분 취소됨", true, true),
    CANCELED("전체 취소됨", false, false);

    private final String description;
    private final boolean cancelable;
    private final boolean refundable;

    PaymentStatus(String description, boolean cancelable, boolean refundable) {
        this.description = description;
        this.cancelable = cancelable;
        this.refundable = refundable;
    }

    public boolean isFinalized() {
        return this == SUCCESS || this == PARTIALLY_CANCELED || this == CANCELED;
    }

    public boolean isSuccessful() {
        return this == SUCCESS || this == PARTIALLY_CANCELED;
    }
}
