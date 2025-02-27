package com.hunnit_beasts.payment.port.out.dto;

import lombok.Getter;

/**
 * 결제 승인 결과 값 객체
 */
@Getter
public class PaymentProcessResult {
    private final boolean success;
    private final String receiptUrl;
    private final String resultCode;
    private final String resultMessage;
    private final String transactionId;

    private PaymentProcessResult(boolean success, String receiptUrl,
                                 String resultCode, String resultMessage,
                                 String transactionId) {
        this.success = success;
        this.receiptUrl = receiptUrl;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.transactionId = transactionId;
    }

    public static PaymentProcessResult success(String receiptUrl, String resultCode,
                                               String resultMessage, String transactionId) {
        return new PaymentProcessResult(true, receiptUrl, resultCode, resultMessage, transactionId);
    }

    public static PaymentProcessResult failure(String resultCode, String resultMessage) {
        return new PaymentProcessResult(false, null, resultCode, resultMessage, null);
    }

}
