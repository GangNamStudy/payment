package com.hunnit_beasts.payment.domain.vo;

import lombok.Getter;

import java.util.Objects;

/**
 * 결제 정보 추상 클래스 (전략 패턴과 함께 사용)
 */
@Getter
public abstract class PaymentInfo {
    private final String type;

    protected PaymentInfo(String type) {
        this.type = Objects.requireNonNull(type, "결제 유형은 필수입니다");
    }
}
