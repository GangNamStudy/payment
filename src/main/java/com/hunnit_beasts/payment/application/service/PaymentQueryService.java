package com.hunnit_beasts.payment.application.service;

import com.hunnit_beasts.payment.application.dto.request.query.PaymentSearchRequestDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import com.hunnit_beasts.payment.application.mapper.PaymentQueryDtoMapper;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import com.hunnit_beasts.payment.port.in.PaymentQueryUseCase;
import com.hunnit_beasts.payment.port.out.PaymentPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 결제 조회 서비스
 * CQRS 패턴을 적용한 조회 전용 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryService implements PaymentQueryUseCase {

    private final PaymentPersistencePort port;
    private final PaymentQueryDtoMapper dtoMapper;

    /**
     * 결제 단건 조회
     * @param paymentId 결제 ID
     * @return 결제 요약 정보 (Optional)
     */
    @Override
    public Optional<PaymentSummaryDto> getPayment(PaymentId paymentId) {
        return port.findById(paymentId)
                .map(dtoMapper::toSummaryDto);
    }

    /**
     * 결제 목록 조회 (검색 + 페이징)
     * @param criteria 검색 조건
     * @return 페이징된 결제 목록
     */
    @Override
    public PaymentListResponseDto getPayments(PaymentSearchRequestDto criteria) {
        Page<Payment> paymentPage = port.search(criteria.toDomain());
        return dtoMapper.toListResponseDto(paymentPage);
    }
}