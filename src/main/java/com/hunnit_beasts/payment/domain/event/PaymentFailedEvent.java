package com.hunnit_beasts.payment.domain.event;

import com.hunnit_beasts.payment.domain.vo.PaymentId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 결제 실패 이벤트
 */
@Getter
public class PaymentFailedEvent implements DomainEvent {
    private final PaymentId paymentId;
    private final String identifier;
    private final String failureReason;
    private final LocalDateTime occurredAt;

    public PaymentFailedEvent(PaymentId paymentId, String identifier, String failureReason) {
        this.paymentId = paymentId;
        this.identifier = identifier;
        this.failureReason = failureReason;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
