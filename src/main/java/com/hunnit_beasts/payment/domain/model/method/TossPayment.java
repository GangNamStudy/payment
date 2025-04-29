package com.hunnit_beasts.payment.domain.model.method;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class TossPayment implements PaymentMethod {
    private final String userId;

    private TossPayment(String userId) {
        this.userId = Objects.requireNonNull(userId, "사용자 ID는 null일 수 없습니다");
        validate();
    }

    public static TossPayment of(String userId) {
        return new TossPayment(userId);
    }

    @Override
    public String getMethodCode() {
        return "TOSS_PAY";
    }

    @Override
    public String getMethodName() {
        return "토스페이";
    }

    @Override
    public void validate() {
        if (userId == null || userId.trim().isEmpty())
            throw new IllegalArgumentException("사용자 ID는 비어있을 수 없습니다");
    }

    @Override
    public Map<String, Object> getProcessingParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.userId);
        return params;
    }

    @Override
    public Map<String, Object> getMaskedInfo() {
        Map<String, Object> masked = new HashMap<>();
        masked.put("methodType", getMethodCode());
        return masked;
    }

}
