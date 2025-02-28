package com.hunnit_beasts.payment.domain.event;

import com.hunnit_beasts.payment.domain.vo.Money;
import com.hunnit_beasts.payment.domain.vo.PaymentId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 결제 생성 이벤트
 */
@Getter
public class PaymentCreatedEvent implements DomainEvent {
    private final PaymentId paymentId;
    private final String identifier;
    private final Money amount;
    private final LocalDateTime occurredAt;

    public PaymentCreatedEvent(PaymentId paymentId, String identifier, Money amount) {
        this.paymentId = paymentId;
        this.identifier = identifier;
        this.amount = amount;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
