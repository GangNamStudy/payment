package com.hunnit_beasts.payment.adapter.out.persistence.repository;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.adapter.out.persistence.mapper.PaymentMapper;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import com.hunnit_beasts.payment.etc.config.annotation.PersistenceAdapter;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;
import com.hunnit_beasts.payment.port.out.PaymentPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentPersistencePort {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity savedEntity = paymentJpaRepository.save(entity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(PaymentId id) {
        return paymentJpaRepository.findById(id.getValue())
                .map(paymentMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdentifier(String identifier) {
        PaymentEntity entity = paymentJpaRepository.findByIdentifier(identifier);
        return Optional.ofNullable(entity)
                .map(paymentMapper::toDomain);
    }

    @Override
    public Page<Payment> search(PaymentSearchCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                createSort(criteria.getSortField(), criteria.getSortDirection())
        );

        String searchTypeStr = criteria.getSearchType() != null ? criteria.getSearchType().name() : null;
        String statusStr = criteria.getStatus() != null ? criteria.getStatus().name() : null;

        Page<PaymentEntity> entityPage = paymentJpaRepository.searchPayments(
                criteria.getSearchText(),
                searchTypeStr,
                criteria.getStartDate(),
                criteria.getEndDate(),
                statusStr,
                criteria.getPaymentType(),
                pageable
        );

        return entityPage.map(paymentMapper::toDomain);
    }

    /**
     * 정렬 조건 생성
     */
    private Sort createSort(String sortField, SortDirection sortDirection) {
        List<String> allowedSortFields = List.of("createdAt", "amount", "orderName", "status", "paymentMethod");

        if (sortField == null || sortField.isEmpty() || !allowedSortFields.contains(sortField))
            return Sort.by(Sort.Direction.DESC, "createdAt");

        Sort.Direction direction = (sortDirection == SortDirection.ASC)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortField);
    }
}