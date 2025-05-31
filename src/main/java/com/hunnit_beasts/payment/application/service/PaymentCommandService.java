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
        log.info("결제 완료 처리 시작: {}", externalIdentifier);

        // 1. 빠른 중복 체크
        Payment existingPayment = findExistingPaymentQuickCheck(externalIdentifier);
        if (existingPayment != null && existingPayment.isCompleted()) {
            log.info("이미 완료된 결제: {}", externalIdentifier);
            return CompletableFuture.completedFuture(paymentDtoMapper.toResponseDto(existingPayment));
        }

        // 2. 외부 API 호출 후 트랜잭션 처리
        return paymentClient.getPayment(externalIdentifier)
                .thenApply(externalPaymentInfo -> {
                    log.info("외부 결제 정보 조회 완료: {}", externalIdentifier);
                    return executePaymentProcessing(externalIdentifier, externalPaymentInfo);
                })
                .exceptionally(e -> {
                    log.error("결제 완료 처리 실패: {}", externalIdentifier, e);
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
            log.debug("결제 처리 트랜잭션 시작: {}", externalIdentifier);

            // 1. 최신 상태 조회
            Payment currentPayment = persistencePort.findByIdentifier(externalIdentifier)
                    .orElse(null);

            // 2. 도메인 로직 처리
            Payment processedPayment = paymentDomainService.completePayment(
                    currentPayment,
                    externalIdentifier,
                    externalPaymentInfo
            );

            // 3. 영속화
            Payment savedPayment = persistencePort.save(processedPayment);

            // 4. 트랜잭션 커밋 후 이벤트 발행 스케줄링
            scheduleEventPublishingAfterCommit();

            log.info("결제 처리 완료: {}", externalIdentifier);
            return paymentDtoMapper.toResponseDto(savedPayment);

        } catch (Exception e) {
            log.error("결제 처리 실패: {}", externalIdentifier, e);
            DomainEvents.clear();
            throw e;
        }
    }

    /**
     * 트랜잭션 커밋 후 이벤트 발행 스케줄링
     * Spring의 TransactionSynchronization을 활용
     */
    private void scheduleEventPublishingAfterCommit() {
        List<DomainEvent> events = DomainEvents.collectEvents();
        if (events.isEmpty()) {
            return;
        }

        log.debug("트랜잭션 커밋 후 이벤트 발행 예약: {} 개", events.size());

        // Spring의 트랜잭션 동기화 기능 활용
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            log.info("트랜잭션 커밋 완료 - 도메인 이벤트 발행 시작: {} 개", events.size());
                            try {
                                // 기존 DomainEventPublisher 사용!
                                eventPublisher.publishAll(events);
                                log.debug("도메인 이벤트 발행 완료");
                            } catch (Exception e) {
                                log.error("도메인 이벤트 발행 실패", e);
                            }
                        }

                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_ROLLED_BACK) {
                                log.warn("트랜잭션 롤백됨 - 이벤트 발행 취소");
                            }
                        }
                    }
            );
        } else {
            // 트랜잭션이 없는 경우 즉시 발행 (fallback)
            log.warn("트랜잭션 동기화가 비활성화됨 - 즉시 이벤트 발행");
            eventPublisher.publishAll(events);
        }
    }
}