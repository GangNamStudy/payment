package com.hunnit_beasts.payment.domain.model.method;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public class CardPayment implements PaymentMethod {
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    private static final Pattern EXPIRY_PATTERN = Pattern.compile("\\d{4}");
    private static final Pattern CVC_PATTERN = Pattern.compile("\\d{3}");

    private final String cardNumber;
    private final String expiry;  // MMYY 형식
    private final String cvc;
    private final Integer installmentMonths;

    private CardPayment(String cardNumber, String expiry, String cvc, Integer installmentMonths) {
        this.cardNumber = Objects.requireNonNull(cardNumber, "카드번호는 null일 수 없습니다");
        this.expiry = Objects.requireNonNull(expiry, "유효기간은 null일 수 없습니다");
        this.cvc = Objects.requireNonNull(cvc, "CVC는 null일 수 없습니다");
        this.installmentMonths = installmentMonths != null ? installmentMonths : 0;
        validate();
    }

    public static CardPayment of(String cardNumber, String expiry, String cvc) {
        return new CardPayment(cardNumber, expiry, cvc, 0);
    }

    public static CardPayment of(String cardNumber, String expiry, String cvc, Integer installmentMonths) {
        return new CardPayment(cardNumber, expiry, cvc, installmentMonths);
    }

    @Override
    public String getMethodCode() {
        return "CARD";
    }

    @Override
    public String getMethodName() {
        return "신용/체크카드";
    }

    @Override
    public void validate() {
        validateCardNumber(this.cardNumber);
        validateExpiry(this.expiry);
        validateCVC(this.cvc);
        validateInstallment(this.installmentMonths);
    }

    private void validateCardNumber(String cardNumber) {
        if (!CARD_NUMBER_PATTERN.matcher(cardNumber).matches())
            throw new IllegalArgumentException("카드번호 형식이 올바르지 않습니다. 예: 1234-5678-9012-3456");
    }

    private void validateExpiry(String expiry) {
        if (!EXPIRY_PATTERN.matcher(expiry).matches())
            throw new IllegalArgumentException("유효기간 형식이 올바르지 않습니다. MMYY 형식이어야 합니다.");

        int month = Integer.parseInt(expiry.substring(0, 2));
        if (month < 1 || month > 12)
            throw new IllegalArgumentException("유효기간의 월이 올바르지 않습니다.");
    }

    private void validateCVC(String cvc) {
        if (!CVC_PATTERN.matcher(cvc).matches())
            throw new IllegalArgumentException("CVC 형식이 올바르지 않습니다. 3자리 숫자여야 합니다.");
    }

    private void validateInstallment(Integer installmentMonths) {
        if (installmentMonths < 0)
            throw new IllegalArgumentException("할부 개월 수는 0 이상이어야 합니다.");
    }

    @Override
    public Map<String, Object> getProcessingParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("cardNumber", this.cardNumber);
        params.put("expiry", this.expiry);
        params.put("cvc", this.cvc);
        params.put("installmentMonths", this.installmentMonths);
        return params;
    }

    @Override
    public Map<String, Object> getMaskedInfo() {
        Map<String, Object> masked = new HashMap<>();
        masked.put("methodType", getMethodCode());
        masked.put("cardNumber", maskCardNumber(this.cardNumber));
        masked.put("installmentMonths", this.installmentMonths);
        return masked;
    }

    private String maskCardNumber(String cardNumber) {
        String[] parts = cardNumber.split("-");
        if (parts.length != 4)
            return "****-****-****-****";
        return parts[0] + "-****-****-" + parts[3];
    }
}
