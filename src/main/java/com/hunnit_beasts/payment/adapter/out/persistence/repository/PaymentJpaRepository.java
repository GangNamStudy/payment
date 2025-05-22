package com.hunnit_beasts.payment.adapter.out.persistence.repository;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {

    PaymentEntity findByIdentifier(String identifier);
}