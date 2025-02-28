package com.hunnit_beasts.payment.domain.event;

import com.hunnit_beasts.payment.domain.vo.Money;
import com.hunnit_beasts.payment.domain.vo.PaymentId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 결제 취소 이벤트
 */
@Getter
public class PaymentCanceledEvent implements DomainEvent {
    private final PaymentId paymentId;
    private final String identifier;
    private final Money canceledAmount;
    private final Money remainingAmount;
    private final LocalDateTime occurredAt;

    public PaymentCanceledEvent(PaymentId paymentId, String identifier,
                                Money canceledAmount, Money remainingAmount) {
        this.paymentId = paymentId;
        this.identifier = identifier;
        this.canceledAmount = canceledAmount;
        this.remainingAmount = remainingAmount;
        this.occurredAt = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}
