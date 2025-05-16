package com.hunnit_beasts.payment.domain.event.payment;

import com.hunnit_beasts.payment.domain.model.payment.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import lombok.Getter;

@Getter
public class PaymentCompletedEvent extends PaymentEvent {
    private final ExternalIdentifier identifier;
    private final Money amount;

    public PaymentCompletedEvent(PaymentId paymentId, ExternalIdentifier identifier, Money amount) {
        super(paymentId);
        this.identifier = identifier;
        this.amount = amount;
    }

    @Override
    public String getEventType() {
        return "PAYMENT_COMPLETED";
    }
}
