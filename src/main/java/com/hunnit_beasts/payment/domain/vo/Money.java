package com.hunnit_beasts.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 금액을 표현하는 값 객체 (기존 Money에서 기능 확장)
 */
@Getter
@EqualsAndHashCode
public class Money {
    private final BigDecimal amount;
    private final String currency;

    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

    public Money(BigDecimal amount, String currency) {
        validateAmount(amount);
        validateCurrency(currency);

        this.amount = amount;
        this.currency = currency;
    }

    /**
     * 원화 금액 생성 팩토리 메서드
     */
    public static Money won(int amount) {
        return new Money(BigDecimal.valueOf(amount), "KRW");
    }

    /**
     * 0원 생성 팩토리 메서드
     */
    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    private void validateAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "금액은 null이 될 수 없습니다");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 0보다 작을 수 없습니다");
        }
    }

    private void validateCurrency(String currency) {
        Objects.requireNonNull(currency, "통화 코드는 null이 될 수 없습니다");
        if (!CURRENCY_PATTERN.matcher(currency).matches()) {
            throw new IllegalArgumentException("통화 코드는 3자리 대문자 영문이어야 합니다");
        }
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("통화가 다른 금액은 비교할 수 없습니다");
        }
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("결과 금액은 0보다 작을 수 없습니다");
        }
        return new Money(result, this.currency);
    }

    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    @Override
    public String toString() {
        return String.format("%s %s", amount.toString(), currency);
    }
}
