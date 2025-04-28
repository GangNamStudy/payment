package com.hunnit_beasts.payment.domain.event.payment;

import com.hunnit_beasts.payment.domain.event.DomainEvent;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public abstract class PaymentEvent implements DomainEvent {
    private final PaymentId paymentId;
    private final LocalDateTime occurredAt;

    protected PaymentEvent(PaymentId paymentId) {
        this.paymentId = Objects.requireNonNull(paymentId, "결제 ID는 null일 수 없습니다");
        this.occurredAt = LocalDateTime.now();
    }
}
