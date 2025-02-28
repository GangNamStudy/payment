package com.hunnit_beasts.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 결제 취소 정보 값 객체
 */
@Getter
@EqualsAndHashCode
public class CancelInfo {
    private final String reason;
    private final Money cancelAmount;
    private final LocalDateTime cancelDateTime;

    public CancelInfo(String reason, Money cancelAmount, LocalDateTime cancelDateTime) {
        this.reason = Objects.requireNonNull(reason, "취소 사유는 필수입니다");
        this.cancelAmount = Objects.requireNonNull(cancelAmount, "취소 금액은 필수입니다");
        this.cancelDateTime = Objects.requireNonNull(cancelDateTime, "취소 일시는 필수입니다");
    }
}
