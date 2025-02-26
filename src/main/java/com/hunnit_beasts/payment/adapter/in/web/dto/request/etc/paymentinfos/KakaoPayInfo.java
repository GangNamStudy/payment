package com.hunnit_beasts.payment.adapter.in.web.dto.request.etc.paymentinfos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayInfo extends PaymentInfo {
    /** 전화번호 */
    private String phoneNumber;

    @Override
    public void validate() {

    }
}
