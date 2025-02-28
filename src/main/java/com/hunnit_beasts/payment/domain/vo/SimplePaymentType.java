package com.hunnit_beasts.payment.domain.vo;

import lombok.Getter;

/**
 * 간편결제 유형 구현
 */
@Getter
public class SimplePaymentType implements PaymentType {
    private final String code;
    private final String description;

    private SimplePaymentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SimplePaymentType kakao() {
        return new SimplePaymentType("KAKAO_PAY", "카카오페이");
    }

    public static SimplePaymentType toss() {
        return new SimplePaymentType("TOSS_PAY", "토스페이");
    }

    @Override
    public void validate(PaymentInfo paymentInfo) {
        if (paymentInfo == null)
            throw new IllegalArgumentException("간편결제에는 간편결제 정보가 필요합니다");

        SimplePaymentInfo simpleInfo = (SimplePaymentInfo) paymentInfo;
        if (!simpleInfo.getType().equals(this.code))
            throw new IllegalArgumentException("간편결제 유형이 일치하지 않습니다");

    }

    @Override
    public boolean canProcess(PaymentInfo paymentInfo) {
        if (paymentInfo == null)
            return false;
        SimplePaymentInfo simpleInfo = (SimplePaymentInfo) paymentInfo;
        return simpleInfo.getType().equals(this.code);
    }
}
