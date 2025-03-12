package com.hunnit_beasts.payment.port.out;

import com.hunnit_beasts.payment.domain.Payment;
import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.vo.PaymentId;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 결제 리포지토리 포트 (출력 포트)
 * 도메인에서 외부 저장소에 접근하기 위한 인터페이스
 */
public interface PaymentRepository {
    /**
     * 결제 정보 저장
     */
    Payment save(Payment payment);

    /**
     * ID로 결제 정보 조회
     */
    Optional<Payment> findById(PaymentId id);

    /**
     * 외부 식별자로 결제 정보 조회
     */
    Optional<Payment> findByIdentifier(String identifier);

    /**
     * 결제 상태로 결제 목록 조회
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * 결제 일시 범위로 결제 목록 조회
     */
    List<Payment> findByPaymentDateTimeBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 검색 조건으로 결제 목록 조회
     */
    List<Payment> search(PaymentSearchCriteria criteria);
}
