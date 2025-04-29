package com.hunnit_beasts.payment.domain.policy;

import com.hunnit_beasts.payment.domain.model.payment.Payment;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 시간 제한 취소 정책
 * 결제 후 일정 시간 내에만 취소 가능
 */
public class TimeLimitedCancellationPolicy implements CancellationPolicy {
    private final Duration cancellationTimeLimit;

    public TimeLimitedCancellationPolicy(Duration cancellationTimeLimit) {
        this.cancellationTimeLimit = cancellationTimeLimit;
    }

    @Override
    public boolean canCancel(Payment payment) {
        // 기본 취소 가능 상태 확인
        if (!payment.isCancelable())
            return false;

        // 결제 완료 시간이 없으면 취소 불가
        if (payment.getCompletedAt() == null)
            return false;

        // 결제 완료 후 지정된 시간이 지났는지 확인
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancellationDeadline = payment.getCompletedAt().plus(cancellationTimeLimit);

        return now.isBefore(cancellationDeadline);
    }
}
