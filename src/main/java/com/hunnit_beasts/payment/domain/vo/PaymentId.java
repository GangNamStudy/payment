package com.hunnit_beasts.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

/**
 * 결제 ID를 나타내는 값 객체
 * UUID 기반으로 생성하여 분산 환경에서도 유니크함을 보장
 */
@Getter
@ToString
@EqualsAndHashCode
public class PaymentId {
    private final String value;

    public PaymentId() {
        this.value = UUID.randomUUID().toString();
    }

    public PaymentId(String value) {
        this.value = Objects.requireNonNull(value, "결제 ID는 null이 될 수 없습니다");
    }
}
