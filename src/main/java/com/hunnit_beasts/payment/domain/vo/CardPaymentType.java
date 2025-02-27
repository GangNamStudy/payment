package com.hunnit_beasts.payment.domain.vo;

/**
 * 카드 결제 유형 구현
 */
public class CardPaymentType implements PaymentType {
    private static final CardPaymentType INSTANCE = new CardPaymentType();

    public static CardPaymentType getInstance() {
        return INSTANCE;
    }

    private CardPaymentType() {}

    @Override
    public String getCode() {
        return "CARD";
    }

    @Override
    public String getDescription() {
        return "카드 결제";
    }

    @Override
    public void validate(PaymentInfo paymentInfo) {
        if (!(paymentInfo instanceof CardPaymentInfo cardInfo)) {
            throw new IllegalArgumentException("카드 결제에는 카드 결제 정보가 필요합니다");
        }

        if (cardInfo.getCardNumber() == null || cardInfo.getExpiry() == null || cardInfo.getCvc() == null) {
            throw new IllegalArgumentException("카드 정보가 부족합니다");
        }
    }

    @Override
    public boolean canProcess(PaymentInfo paymentInfo) {
        return paymentInfo instanceof CardPaymentInfo;
    }
}
