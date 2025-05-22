package com.hunnit_beasts.payment.application.service;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.application.mapper.PaymentDomainMapper;
import com.hunnit_beasts.payment.application.mapper.PaymentDtoMapper;
import com.hunnit_beasts.payment.domain.event.DomainEvent;
import com.hunnit_beasts.payment.domain.event.DomainEvents;
import com.hunnit_beasts.payment.domain.event.payment.PaymentCompletedEvent;
import com.hunnit_beasts.payment.domain.model.money.Money;
import com.hunnit_beasts.payment.domain.model.payment.ExternalIdentifier;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.PaymentProcessingInfo;
import com.hunnit_beasts.payment.port.in.PaymentCommendUseCase;
import com.hunnit_beasts.payment.port.out.DomainEventPublisher;
import com.hunnit_beasts.payment.port.out.PaymentClient;
import com.hunnit_beasts.payment.port.out.PaymentPersistencePort;
import io.portone.sdk.server.payment.PaidPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCommandService implements PaymentCommendUseCase {

    private final PaymentClient paymentClient;
    private final PaymentPersistencePort persistencePort;
    private final DomainEventPublisher eventPublisher;
    private final PaymentDtoMapper paymentDtoMapper;
    private final PaymentDomainMapper paymentDomainMapper;

    @Override
    public PaymentCancelResponseDto cancelPayment(String paymentId, PaymentCancelRequestDto request) {
        throw new UnsupportedOperationException("아직 구현되지 않음");
    }

    @Override
    @Transactional
    public CompletableFuture<PaymentResponseDto> completePayment(String externalIdentifier) {
        Payment existingPayment = persistencePort.findByIdentifier(externalIdentifier)
                .orElse(null);

        if (existingPayment != null && existingPayment.isCompleted())
            return CompletableFuture.completedFuture(paymentDtoMapper.toResponseDto(existingPayment));

        // 포트원 API로 결제 정보 조회
        return paymentClient.getPayment(externalIdentifier)
                .thenApply(payment -> {
                    if (payment instanceof PaidPayment paidPayment) {
                        Payment completedPayment = processPayment(externalIdentifier, paidPayment);

                        List<DomainEvent> events = DomainEvents.collectEvents();
                        eventPublisher.publishAll(events);

                        return paymentDtoMapper.toResponseDto(completedPayment);
                    } else
                        throw new IllegalStateException("결제가 완료되지 않았습니다: " + externalIdentifier);
                })
                .exceptionally(e -> {
                    throw new RuntimeException("결제 정보 조회 중 오류 발생", e);
                });
    }

    /**
     * 결제 정보 처리 (생성 또는 업데이트)
     */
    private Payment processPayment(String externalIdentifier, PaidPayment paidPayment) {
        Payment payment = persistencePort.findByIdentifier(externalIdentifier)
                .orElseGet(() -> createNewPayment(externalIdentifier, paidPayment));

        Payment completedPayment = payment.complete(
                paidPayment.getReceiptUrl(),
                paidPayment.getPgResponse()
        );

        if (completedPayment != payment) {
            DomainEvents.publish(new PaymentCompletedEvent(
                    completedPayment.getId(),
                    ExternalIdentifier.of(externalIdentifier),
                    Money.won((int) paidPayment.getAmount().getTotal())
            ));

            return persistencePort.save(completedPayment);
        }

        return payment;
    }

    /**
     * 새 결제 정보 생성
     */
    private Payment createNewPayment(String externalIdentifier, PaidPayment paidPayment) {
        PaymentProcessingInfo processingInfo = paymentDomainMapper.mapToProcessingInfo(paidPayment);

        Payment payment = Payment.createFromPortone(
                externalIdentifier,
                paidPayment.getOrderName(),
                (int) paidPayment.getAmount().getTotal(),
                processingInfo,
                paidPayment.getPgResponse()
        );

        return persistencePort.save(payment);
    }
}