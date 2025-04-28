package com.hunnit_beasts.payment.domain.event.payment;

import com.hunnit_beasts.payment.domain.model.identifier.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import lombok.Getter;

@Getter
public class PaymentCanceledEvent extends PaymentEvent {
    private final ExternalIdentifier identifier;
    private final Money canceledAmount;
    private final Money remainingAmount;
    private final String reason;

    public PaymentCanceledEvent(PaymentId paymentId, ExternalIdentifier identifier,
                                Money canceledAmount, Money remainingAmount, String reason) {
        super(paymentId);
        this.identifier = identifier;
        this.canceledAmount = canceledAmount;
        this.remainingAmount = remainingAmount;
        this.reason = reason;
    }

    @Override
    public String getEventType() {
        return "PAYMENT_CANCELED";
    }
}
