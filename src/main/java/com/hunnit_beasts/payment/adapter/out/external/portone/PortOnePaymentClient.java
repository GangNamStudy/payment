package com.hunnit_beasts.payment.adapter.out.external.portone;

import com.hunnit_beasts.payment.domain.model.external.ExternalPaymentInfo;
import com.hunnit_beasts.payment.port.out.PaymentClient;
import io.portone.sdk.server.payment.PaidPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class PortOnePaymentClient implements PaymentClient {

    private final io.portone.sdk.server.payment.PaymentClient portone;

    public PortOnePaymentClient(PortOneProperties properties) {
        this.portone = new io.portone.sdk.server.payment.PaymentClient(
                properties.getApi(),
                "https://api.portone.io",
                properties.getStoreId()
        );
    }

    @Override
    public CompletableFuture<ExternalPaymentInfo> getPayment(String paymentId) {
        return portone.getPayment(paymentId)
                .thenApply(payment -> {
                    if (payment instanceof PaidPayment paidPayment) {
                        log.info("PortOne 결제 정보 조회 성공: paymentId={}", paymentId);
                        return new PortOnePaymentInfo(paidPayment);
                    } else {
                        log.warn("결제가 완료되지 않은 상태: paymentId={}, type={}",
                                paymentId, payment.getClass().getSimpleName());
                        throw new IllegalStateException("결제가 완료되지 않았습니다: " + paymentId);
                    }
                })
                .handle((result, throwable) -> {
                    if (throwable != null) {
                        log.error("PortOne 결제 정보 조회 실패: paymentId={}", paymentId, throwable);
                        throw new RuntimeException("결제 정보 조회 중 오류 발생: " + paymentId, throwable);
                    }
                    return result;
                });
    }
}