package com.hunnit_beasts.payment.adapter.in.web.dto.request.etc.paymentinfos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossPayInfo extends PaymentInfo {
    /** 토스 계정 ID */
    private String tossAccountId;

    @Override
    public void validate() {

    }
}
