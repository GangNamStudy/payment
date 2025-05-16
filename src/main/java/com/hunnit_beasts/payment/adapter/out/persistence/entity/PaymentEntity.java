package com.hunnit_beasts.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 통합된 결제 JPA 엔티티
 */
@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payments_identifier", columnList = "identifier", unique = true),
                @Index(name = "idx_payments_status", columnList = "status"),
                @Index(name = "idx_payments_payment_method", columnList = "payment_method"),
                @Index(name = "idx_payments_created_at", columnList = "created_at"),
                @Index(name = "idx_payments_completed_at", columnList = "completed_at"),
                @Index(name = "idx_payments_order_name", columnList = "order_name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {
    @Id
    @Column(length = 36)
    @Comment("결제 고유 식별자 (UUID)")
    private String id;  // PaymentId의 value 필드

    @Column(nullable = false, length = 50, unique = true)
    @Comment("외부 시스템 식별자 (포트원 결제 ID)")
    private String identifier;  // 외부 시스템 식별자

    @Column(nullable = false, length = 200)
    @Comment("주문명")
    private String orderName;  // 주문명

    @Column(nullable = false, precision = 19, scale = 2)
    @Comment("결제 금액")
    private BigDecimal amount;  // 결제 금액

    @Column(nullable = false, length = 3)
    @Comment("통화 코드 (ISO 4217)")
    private String currency;  // 통화 코드

    // 결제 처리 정보
    @Column(length = 100)
    @Comment("트랜잭션 ID")
    private String transactionId;  // 트랜잭션 ID

    @Column(length = 50)
    @Comment("PG사 제공자")
    private String pgProvider;  // PG사 제공자

    @Column(length = 100)
    @Comment("PG사 트랜잭션 ID")
    private String pgTxId;  // PG사 트랜잭션 ID

    @Column(nullable = false, length = 30, name = "payment_method")
    @Comment("결제 수단 코드")
    private String paymentMethod;  // 결제 수단 코드

    // 결제 방법 상세 정보 (필요시에만 사용)
    @Column(length = 50)
    @Comment("결제 제공자 (카드사, 간편결제 서비스 등)")
    private String methodProvider;  // 결제 제공자

    @Column(length = 25)
    @Comment("마스킹된 카드 번호")
    private String maskedCardNumber;  // 마스킹된 카드 번호

    @Column
    @Comment("할부 개월 수")
    private Integer installmentMonths;  // 할부 개월 수

    // 결제 상태 정보
    @Column(nullable = false, length = 20)
    @Comment("결제 상태 (PENDING, SUCCESS, FAILED)")
    private String status;  // 결제 상태 (PaymentStatus enum의 name())

    @Column(nullable = false)
    @Comment("생성 시간")
    private LocalDateTime createdAt;  // 생성 시간

    @Column
    @Comment("완료 시간")
    private LocalDateTime completedAt;  // 완료 시간

    @Column(length = 512)
    @Comment("영수증 URL")
    private String receiptUrl;  // 영수증 URL

    @Column(columnDefinition = "TEXT")
    @Comment("PG 응답 정보 (JSON 형식)")
    private String pgResponse;  // PG 응답 정보 - JSON으로 저장

    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시간")
    private LocalDateTime updatedAt;  // 엔티티 수정 시간

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}