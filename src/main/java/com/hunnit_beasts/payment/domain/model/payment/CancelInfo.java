package com.hunnit_beasts.payment.domain.model.payment;

import com.hunnit_beasts.payment.domain.model.money.Money;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class CancelInfo {
    private final String reason;
    private final Money amount;
    private final LocalDateTime canceledAt;

    private CancelInfo(String reason, Money amount, LocalDateTime canceledAt) {
        if (reason == null || reason.trim().isEmpty())
            throw new IllegalArgumentException("취소 사유는 null이거나 빈 값일 수 없습니다");
        this.reason = reason;
        this.amount = Objects.requireNonNull(amount, "취소 금액은 null일 수 없습니다");
        this.canceledAt = Objects.requireNonNull(canceledAt, "취소 시간은 null일 수 없습니다");
    }

    public static CancelInfo of(String reason, Money amount) {
        return new CancelInfo(reason, amount, LocalDateTime.now());
    }

    public static CancelInfo of(String reason, Money amount, LocalDateTime canceledAt) {
        return new CancelInfo(reason, amount, canceledAt);
    }

    public String getReason() {
        return reason;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDateTime getCanceledAt() {
        return canceledAt;
    }
}
