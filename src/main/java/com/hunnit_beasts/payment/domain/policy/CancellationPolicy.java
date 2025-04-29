package com.hunnit_beasts.payment.domain.policy;

import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.Payment;

/**
 * 취소 정책 인터페이스
 * 다양한 취소 정책 구현을 위한 전략 패턴
 */
public interface CancellationPolicy {

    /**
     * 결제가 취소 가능한지 판단
     */
    boolean canCancel(Payment payment);

    /**
     * 취소 수수료 계산 (필요한 경우)
     */
    default Money calculateCancellationFee(Payment payment) {
        return Money.zero(payment.getAmount().getCurrency());
    }
}
