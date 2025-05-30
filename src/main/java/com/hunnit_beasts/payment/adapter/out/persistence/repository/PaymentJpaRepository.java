package com.hunnit_beasts.payment.adapter.out.persistence.repository;

import com.hunnit_beasts.payment.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {

    /**
     * 외부 식별자로 결제 조회 (기존)
     */
    PaymentEntity findByIdentifier(String identifier);

    /**
     * 🆕 동적 검색 쿼리 - 모든 조건을 하나의 JPQL로 처리
     *
     * @param searchText 검색어 (null 가능)
     * @param searchType 검색 타입 (PAYMENT_ID, IDENTIFIER, ORDER_NAME)
     * @param startDate 시작날짜 (null 가능)
     * @param endDate 종료날짜 (null 가능)
     * @param status 결제상태 (null 가능)
     * @param paymentMethod 결제수단 (null 가능)
     * @param pageable 페이징 정보
     * @return 페이징된 결제 목록
     */
    @Query("""
    SELECT p FROM PaymentEntity p
    WHERE 1=1
        AND (:searchText IS NULL OR 
            CASE :searchType
                WHEN 'PAYMENT_ID' THEN p.id LIKE %:searchText%
                WHEN 'IDENTIFIER' THEN p.identifier LIKE %:searchText%
                WHEN 'ORDER_NAME' THEN p.orderName LIKE %:searchText%
                ELSE (p.id LIKE %:searchText% OR p.identifier LIKE %:searchText% OR p.orderName LIKE %:searchText%)
            END)
        AND (:startDate IS NULL OR p.createdAt >= :startDate)
        AND (:endDate IS NULL OR p.createdAt <= :endDate)
        AND (:status IS NULL OR p.status = :status)
        AND (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod)
    """)
    Page<PaymentEntity> searchPayments(
            @Param("searchText") String searchText,
            @Param("searchType") String searchType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") String status,
            @Param("paymentMethod") String paymentMethod,
            Pageable pageable
    );
}