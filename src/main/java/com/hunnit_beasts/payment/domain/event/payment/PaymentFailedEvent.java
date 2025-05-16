package com.hunnit_beasts.payment.domain.event.payment;

import com.hunnit_beasts.payment.domain.model.payment.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import lombok.Getter;

@Getter
public class PaymentFailedEvent extends PaymentEvent {
    private final ExternalIdentifier identifier;
    private final String failureReason;

    public PaymentFailedEvent(PaymentId paymentId, ExternalIdentifier identifier, String failureReason) {
        super(paymentId);
        this.identifier = identifier;
        this.failureReason = failureReason;
    }

    @Override
    public String getEventType() {
        return "PAYMENT_FAILED";
    }
}
