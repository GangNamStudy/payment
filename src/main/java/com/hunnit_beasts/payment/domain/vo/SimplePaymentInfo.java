package com.hunnit_beasts.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 간편결제 정보
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class SimplePaymentInfo extends PaymentInfo {
    private final String userId;

    private SimplePaymentInfo(String type, String userId) {
        super(type);
        this.userId = userId;
    }

    public static SimplePaymentInfo kakao(String userId) {
        return new SimplePaymentInfo("KAKAO_PAY", userId);
    }

    public static SimplePaymentInfo toss(String userId) {
        return new SimplePaymentInfo("TOSS_PAY", userId);
    }
}