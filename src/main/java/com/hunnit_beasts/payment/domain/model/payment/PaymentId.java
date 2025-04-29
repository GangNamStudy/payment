package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public final class PaymentId {
    private final String value;

    private PaymentId(String value) {
        this.value = Objects.requireNonNull(value, "결제 ID는 null일 수 없습니다");
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
    }

    public static PaymentId of(String value) {
        return new PaymentId(value);
    }
}