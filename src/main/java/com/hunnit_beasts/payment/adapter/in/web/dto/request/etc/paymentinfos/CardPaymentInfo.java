package com.hunnit_beasts.payment.adapter.in.web.dto.request.etc.paymentinfos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardPaymentInfo extends PaymentInfo {
    /** 카드 번호 */
    private String cardNumber;
    /** 유효기간 */
    private String expiry;
    /** CVC */
    private String cvc;
    /** 할부 개월 */
    private Integer installmentMonths;

    @Override
    public void validate() {

    }
}
