package com.hunnit_beasts.payment.domain.model.money;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class Currency {
    public static final Currency KRW = new Currency("KRW", "원", 0);
    public static final Currency USD = new Currency("USD", "$", 2);

    private final String code;
    private final String symbol;
    private final int defaultFractionDigits;

    private Currency(String code, String symbol, int defaultFractionDigits) {
        this.code = Objects.requireNonNull(code, "통화 코드는 null일 수 없습니다");
        this.symbol = Objects.requireNonNull(symbol, "통화 기호는 null일 수 없습니다");
        this.defaultFractionDigits = defaultFractionDigits;
    }

}

