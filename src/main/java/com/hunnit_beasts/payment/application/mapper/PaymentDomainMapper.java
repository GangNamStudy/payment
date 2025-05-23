package com.hunnit_beasts.payment.application.mapper;

import com.hunnit_beasts.payment.domain.model.payment.PaymentProcessingInfo;
import io.portone.sdk.server.payment.PaidPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 외부 결제 시스템의 응답을 도메인 객체로 매핑하는 매퍼
 */
@Slf4j
@Component
public class PaymentDomainMapper {

    /**
     * 결제 수단 정보 추출
     */
    public String extractPaymentMethod(PaidPayment paidPayment) {
        String paymentMethod = "UNKNOWN";

        try {
            // 메소드 객체에서 제공자 추출 시도
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

            // PG 응답에서 결제 방식 확인 (위에서 실패한 경우)
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
            log.error("결제 방식 추출 중 오류 발생", e);
        }

        // 결제 방식이 여전히 UNKNOWN이면 기본값 설정
        if ("UNKNOWN".equals(paymentMethod))
            paymentMethod = "Kakaopay"; // 기본값 설정

        return paymentMethod;
    }

    /**
     * 외부 결제 정보에서 결제 처리 정보 추출
     */
    public PaymentProcessingInfo mapToProcessingInfo(PaidPayment paidPayment) {
        return PaymentProcessingInfo.of(
                paidPayment.getTransactionId(),
                paidPayment.getChannel().getPgProvider().getValue(),
                paidPayment.getPgTxId(),
                extractPaymentMethod(paidPayment)
        );
    }
}