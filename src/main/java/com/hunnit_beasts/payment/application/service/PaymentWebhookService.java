package com.hunnit_beasts.payment.application.service;

import com.hunnit_beasts.payment.domain.event.DomainEvents;
import com.hunnit_beasts.payment.port.in.PaymentCommendUseCase;
import com.hunnit_beasts.payment.port.in.PaymentWebhookUseCase;
import com.hunnit_beasts.payment.port.out.WebhookVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService implements PaymentWebhookUseCase {

    private final WebhookVerifier webhookVerifier;
    private final PaymentCommendUseCase paymentCommendUseCase;

    @Override
    @Transactional
    public CompletableFuture<Void> handleWebhook(
            String impUid,
            String merchantUid,
            String status) {

        try {
            log.info("포트원 결제 ID: {}", impUid);
            log.info("가맹점 주문 ID: {}", merchantUid);
            log.info("결제 상태: {}", status);

            switch (status) {
                case "paid":
                    log.info("결제 완료 웹훅 수신 - 결제 처리 로직 실행 예정");
                    break;
                case "failed":
                    log.info("결제 실패 웹훅 수신 - 실패 처리 로직 실행 예정");
                    break;
                case "cancelled":
                    log.info("결제 취소 웹훅 수신 - 취소 처리 로직 실행 예정");
                    break;
                default:
                    log.info("알 수 없는 결제 상태: {}", status);
            }

            log.info("웹훅 처리 완료: impUid={}", impUid);
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("웹훅 처리 중 오류 발생: impUid={}, merchantUid={}, status={}", impUid, merchantUid, status, e);
            return CompletableFuture.failedFuture(new RuntimeException("웹훅 처리 실패", e));
        } finally {
            DomainEvents.clear();
        }
    }
}