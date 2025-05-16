package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class ExternalIdentifier {
    private final String value;

    private ExternalIdentifier(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("외부 식별자는 null이거나 빈 값일 수 없습니다");
        this.value = value;
    }

    public static ExternalIdentifier of(String value) {
        return new ExternalIdentifier(value);
    }
}
