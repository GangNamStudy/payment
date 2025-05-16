package com.hunnit_beasts.payment.domain.model.payment;

import lombok.Getter;

/**
 * 결제 상태를 나타내는 열거형
 */
@Getter
public enum PaymentStatus {
    PENDING("결제 진행 중"),
    AUTHORIZED("결제 승인됨"),
    SUCCESS("결제 성공"),
    FAILED("결제 실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    /**
     * 결제가 최종 상태인지 여부
     */
    public boolean isFinalized() {
        return this == SUCCESS || this == FAILED;
    }

    /**
     * 결제가 성공적으로 완료되었는지 여부
     */
    public boolean isSuccessful() {
        return this == SUCCESS;
    }
}