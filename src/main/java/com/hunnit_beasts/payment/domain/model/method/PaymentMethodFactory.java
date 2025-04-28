package com.hunnit_beasts.payment.domain.model.method;

import java.util.Map;

public class PaymentMethodFactory {

    public static PaymentMethod createFromPayload(Map<String, Object> payload) {
        String methodType = (String) payload.get("type");

        if (methodType == null) {
            throw new IllegalArgumentException("결제 수단 타입이 필요합니다");
        }

        return switch (methodType.toUpperCase()) {
            case "CARD" -> createCardPayment(payload);
            case "KAKAO_PAY" -> createKakaoPayment(payload);
            case "TOSS_PAY" -> createTossPayment(payload);
            default -> throw new IllegalArgumentException("지원하지 않는 결제 수단입니다: " + methodType);
        };
    }

    private static CardPayment createCardPayment(Map<String, Object> payload) {
        String cardNumber = (String) payload.get("cardNumber");
        String expiry = (String) payload.get("expiry");
        String cvc = (String) payload.get("cvc");
        Integer installmentMonths = (Integer) payload.getOrDefault("installmentMonths", 0);

        return CardPayment.of(cardNumber, expiry, cvc, installmentMonths);
    }

    private static KakaoPayment createKakaoPayment(Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        return KakaoPayment.of(userId);
    }

    private static TossPayment createTossPayment(Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        return TossPayment.of(userId);
    }
}
