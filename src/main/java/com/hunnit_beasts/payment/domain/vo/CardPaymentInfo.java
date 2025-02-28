package com.hunnit_beasts.payment.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 카드 결제 정보
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class CardPaymentInfo extends PaymentInfo {
    private final String cardNumber;
    private final String expiry;
    private final String cvc;
    private final Integer installmentMonths;

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("\\d{4}");
    private static final Pattern CVC_PATTERN = Pattern.compile("\\d{3}");

    private CardPaymentInfo(String cardNumber, String expiry,
                            String cvc, Integer installmentMonths) {
        super("CARD");
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvc = cvc;
        this.installmentMonths = installmentMonths != null ? installmentMonths : 0;
    }

    public static CardPaymentInfo of(String cardNumber, String expiry,
                                     String cvc, Integer installmentMonths) {
        validateCardNumber(cardNumber);
        validateExpiry(expiry);
        validateCVC(cvc);
        validateInstallmentMonths(installmentMonths);

        return new CardPaymentInfo(cardNumber, expiry, cvc, installmentMonths);
    }

    private static void validateCardNumber(String cardNumber) {
        Objects.requireNonNull(cardNumber, "카드 번호는 필수입니다");
        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            throw new IllegalArgumentException("카드 번호 형식이 올바르지 않습니다");
        }
    }

    private static void validateExpiry(String expiry) {
        Objects.requireNonNull(expiry, "유효기간은 필수입니다");
        if (!EXPIRY_PATTERN.matcher(expiry).matches()) {
            throw new IllegalArgumentException("유효기간 형식이 올바르지 않습니다");
        }
    }

    private static void validateCVC(String cvc) {
        Objects.requireNonNull(cvc, "CVC는 필수입니다");
        if (!CVC_PATTERN.matcher(cvc).matches()) {
            throw new IllegalArgumentException("CVC 형식이 올바르지 않습니다");
        }
    }

    private static void validateInstallmentMonths(Integer installmentMonths) {
        if (installmentMonths != null && installmentMonths < 0) {
            throw new IllegalArgumentException("할부 개월 수는 0 이상이어야 합니다");
        }
    }
}
