package com.hunnit_beasts.payment.adapter.out.external.portone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class PortOnePaymentClient implements com.hunnit_beasts.payment.port.out.PaymentClient {

    private final io.portone.sdk.server.payment.PaymentClient portone;

    public PortOnePaymentClient(PortOneProperties properties) {
        this.portone = new io.portone.sdk.server.payment.PaymentClient(
                properties.getApi(),  // API 시크릿
                "https://api.portone.io",  // API 기본 URL
                properties.getStoreId()  // 상점 ID
        );
    }

    @Override
    public CompletableFuture<?> getPayment(String paymentId) {
        return portone.getPayment(paymentId);
    }
}