package com.hunnit_beasts.payment.adapter.in.web.dto.request.etc.paymentinfos;

import com.hunnit_beasts.payment.domain.enums.PaymentMethod;
import lombok.Getter;

@Getter
public abstract class PaymentInfo {
    private PaymentMethod type;

    public abstract void validate();
}
