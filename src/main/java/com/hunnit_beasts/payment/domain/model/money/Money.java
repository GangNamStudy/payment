package com.hunnit_beasts.payment.domain.model.money;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@ToString
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    private Money(BigDecimal amount, Currency currency) {
        validateAmount(amount);
        this.amount = amount.setScale(currency.getDefaultFractionDigits());
        this.currency = Objects.requireNonNull(currency, "통화는 null일 수 없습니다");
    }

    private void validateAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "금액은 null일 수 없습니다");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("금액은 음수일 수 없습니다");
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money won(int amount) {
        return new Money(BigDecimal.valueOf(amount), Currency.KRW);
    }

    public static Money won(BigDecimal amount) {
        return new Money(amount, Currency.KRW);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money money) {
        validateSameCurrency(money);
        return new Money(this.amount.add(money.amount), this.currency);
    }

    public Money subtract(Money money) {
        validateSameCurrency(money);
        BigDecimal result = this.amount.subtract(money.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("뺄셈 결과가 음수일 수 없습니다");
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean equals(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException(String.format("통화가 다릅니다: %s와 %s", this.currency, other.currency));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
