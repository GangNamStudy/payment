package com.hunnit_beasts.payment.adapter.out.persistence.repository;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import com.hunnit_beasts.payment.adapter.out.persistence.mapper.PaymentMapper;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import com.hunnit_beasts.payment.domain.model.payment.PaymentId;
import com.hunnit_beasts.payment.etc.config.annotation.PersistenceAdapter;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;
import com.hunnit_beasts.payment.port.out.PaymentPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    public List<Payment> search(PaymentSearchCriteria criteria) {
        return List.of();
    }
}