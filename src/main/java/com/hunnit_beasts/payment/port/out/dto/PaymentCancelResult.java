package com.hunnit_beasts.payment.port.out.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 취소 결과 값 객체
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCancelResult {
    private final boolean success;
    private final String resultCode;
    private final String resultMessage;
    private final String transactionId;

    public static PaymentCancelResult success(String resultCode,
                                              String resultMessage, String transactionId) {
        return new PaymentCancelResult(true, resultCode, resultMessage, transactionId);
    }

    public static PaymentCancelResult failure(String resultCode, String resultMessage) {
        return new PaymentCancelResult(false, resultCode, resultMessage, null);
    }

}
