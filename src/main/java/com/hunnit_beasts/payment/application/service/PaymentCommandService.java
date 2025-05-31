package com.hunnit_beasts.payment.application.service;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.application.mapper.PaymentDtoMapper;
import com.hunnit_beasts.payment.domain.event.DomainEvent;
import com.hunnit_beasts.payment.domain.event.DomainEvents;
import com.hunnit_beasts.payment.domain.model.external.ExternalPaymentInfo;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.service.PaymentDomainService;
import com.hunnit_beasts.payment.port.in.PaymentCommendUseCase;
import com.hunnit_beasts.payment.port.out.DomainEventPublisher;
import com.hunnit_beasts.payment.port.out.PaymentClient;
import com.hunnit_beasts.payment.port.out.PaymentPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 결제 커맨드 애플리케이션 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCommandService implements PaymentCommendUseCase {

    private final PaymentClient paymentClient;
    private final PaymentPersistencePort persistencePort;
    private final DomainEventPublisher eventPublisher; // 기존 DomainEventPublisher 사용
    private final PaymentDtoMapper paymentDtoMapper;
    private final PaymentDomainService paymentDomainService;

    @Override
    public PaymentCancelResponseDto cancelPayment(String paymentId, PaymentCancelRequestDto request) {
        throw new UnsupportedOperationException("아직 구현되지 않음");
    }

    @Override
    public CompletableFuture<PaymentResponseDto> completePayment(String externalIdentifier) {
        Payment existingPayment = findExistingPaymentQuickCheck(externalIdentifier);
        if (existingPayment != null && existingPayment.isCompleted())
            return CompletableFuture.completedFuture(paymentDtoMapper.toResponseDto(existingPayment));

        // 2. 외부 API 호출 후 트랜잭션 처리
        return paymentClient.getPayment(externalIdentifier)
                .thenApply(externalPaymentInfo -> {
                    return executePaymentProcessing(externalIdentifier, externalPaymentInfo);
                })
                .exceptionally(e -> {
                    throw new RuntimeException("결제 완료 처리 중 오류: " + externalIdentifier, e);
                });
    }

    @Transactional(readOnly = true)
    public Payment findExistingPaymentQuickCheck(String externalIdentifier) {
        return persistencePort.findByIdentifier(externalIdentifier).orElse(null);
    }

    /**
     * 트랜잭션 안에서 결제 처리 + 이벤트 발행 스케줄링
     */
    @Transactional
    public PaymentResponseDto executePaymentProcessing(
            String externalIdentifier,
            ExternalPaymentInfo externalPaymentInfo) {

        try {
            Payment currentPayment = persistencePort.findByIdentifier(externalIdentifier)
                    .orElse(null);

            Payment processedPayment = paymentDomainService.completePayment(
                    currentPayment,
                    externalIdentifier,
                    externalPaymentInfo
            );


            Payment savedPayment = persistencePort.save(processedPayment);
            scheduleEventPublishingAfterCommit();
            return paymentDtoMapper.toResponseDto(savedPayment);

        } catch (Exception e) {
            DomainEvents.clear();
            throw e;
        }
    }

    /**
     * 트랜잭션 커밋 후 이벤트 발행 스케줄링
     * Spring의 TransactionSynchronization을 활용
     */
    private void scheduleEventPublishingAfterCommit() {
        // 1. 도메인에서 발생한 이벤트들 수집
        List<DomainEvent> events = DomainEvents.collectEvents();
        if (events.isEmpty()) return;  // 이벤트 없으면 종료

        // 2. 트랜잭션이 활성화되어 있는지 확인
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            // 3. 트랜잭션 커밋 후에 실행될 콜백 등록
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            // 트랜잭션이 성공적으로 커밋된 후 실행
                            try {
                                eventPublisher.publishAll(events);
                            } catch (Exception e) {
                                log.error("도메인 이벤트 발행 실패", e);
                            }
                        }

                        @Override
                        public void afterCompletion(int status) {
                            // 트랜잭션이 완전히 끝난 후 실행
                            if (status == STATUS_ROLLED_BACK)
                                log.warn("트랜잭션 롤백됨 - 이벤트 발행 취소");
                        }
                    }
            );
        } else {
            // 트랜잭션이 없는 경우 즉시 발행
            eventPublisher.publishAll(events);
        }
    }
}