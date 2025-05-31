package com.hunnit_beasts.payment.adapter.out.external.portone;

import com.hunnit_beasts.payment.domain.model.external.ExternalPaymentInfo;
import io.portone.sdk.server.payment.PaidPayment;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * PortOne의 PaidPayment를 도메인의 ExternalPaymentInfo로 변환하는 어댑터
 * Anti-Corruption Layer 역할
 */
@RequiredArgsConstructor
public class PortOnePaymentInfo implements ExternalPaymentInfo {

    private final PaidPayment paidPayment;

    @Override
    public String getTransactionId() {
        return paidPayment.getTransactionId();
    }

    @Override
    public String getOrderName() {
        return paidPayment.getOrderName();
    }

    @Override
    public BigDecimal getTotalAmount() {
        return BigDecimal.valueOf(paidPayment.getAmount().getTotal());
    }

    @Override
    public String getReceiptUrl() {
        return paidPayment.getReceiptUrl();
    }

    @Override
    public String getPgProvider() {
        return paidPayment.getChannel().getPgProvider().getValue();
    }

    @Override
    public String getPgTxId() {
        return paidPayment.getPgTxId();
    }

    @Override
    public String getPaymentMethod() {
        return extractPaymentMethod();
    }

    @Override
    public String getPgResponse() {
        return paidPayment.getPgResponse();
    }

    @Override
    public boolean isPaymentCompleted() {
        // PortOne의 PaidPayment는 이미 결제 완료된 상태
        return true;
    }

    /**
     * 결제 수단 정보 추출 (기존 PaymentDomainMapper 로직 활용)
     */
    private String extractPaymentMethod() {
        String paymentMethod = "UNKNOWN";

        try {
            if (paidPayment.getMethod() != null) {
                String methodStr = paidPayment.getMethod().toString();

                if (methodStr.contains("provider=")) {
                    int start = methodStr.indexOf("provider=") + 9;
                    int end = methodStr.indexOf(",", start);
                    if (end == -1) {
                        end = methodStr.indexOf(")", start);
                    }
                    if (start > 9 && end > start) {
                        paymentMethod = methodStr.substring(start, end);
                    }
                }
            }

            if ("UNKNOWN".equals(paymentMethod) && paidPayment.getPgResponse() != null) {
                String pgResponse = paidPayment.getPgResponse();
                if (pgResponse.contains("\"payMethod\": \"")) {
                    int start = pgResponse.indexOf("\"payMethod\": \"") + 13;
                    int end = pgResponse.indexOf("\"", start);
                    if (start > 13 && end > start) {
                        paymentMethod = pgResponse.substring(start, end);
                    }
                }
            }
        } catch (Exception e) {
            // 로깅은 어댑터 계층에서 처리
        }

        return "UNKNOWN".equals(paymentMethod) ? "Kakaopay" : paymentMethod;
    }
}