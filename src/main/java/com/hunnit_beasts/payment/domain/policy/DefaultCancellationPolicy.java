package com.hunnit_beasts.payment.domain.policy;

import com.hunnit_beasts.payment.domain.model.payment.Payment;

/**
 * 기본 취소 정책 구현
 * 결제 상태에 따른 기본적인 취소 가능 여부만 판단
 */
public class DefaultCancellationPolicy implements CancellationPolicy {

    @Override
    public boolean canCancel(Payment payment) {
        return payment.isCancelable();
    }
}
