package com.hunnit_beasts.payment.domain.enums;

import lombok.Getter;

/**
 * 결제 상태 열거형 (상태 추가로 확장)
 */
@Getter
public enum PaymentStatus {
    PENDING("결제 진행 중"),
    SUCCESS("결제 성공"),
    FAILED("결제 실패"),
    PARTIALLY_CANCELED("부분 취소됨"),
    CANCELED("전체 취소됨"),
    REFUNDED("환불됨");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

}
