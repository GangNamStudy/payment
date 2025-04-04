package com.hunnit_beasts.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 결제 JPA 엔티티
 */
@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payments_identifier", columnList = "identifier", unique = true),
                @Index(name = "idx_payments_status", columnList = "status"),
                @Index(name = "idx_payments_payment_type_code", columnList = "payment_type_code"),
                @Index(name = "idx_payments_payment_date_time", columnList = "payment_date_time"),
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
    @Comment("외부 시스템 식별자")
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

    @Column(nullable = false, length = 20)
    @Comment("결제 유형 코드 (CARD, KAKAO_PAY, TOSS_PAY 등)")
    private String paymentTypeCode;  // 결제 유형 코드

    @Column(nullable = false, length = 20)
    @Comment("결제 상태 (PENDING, SUCCESS, FAILED, PARTIALLY_CANCELED, CANCELED, REFUNDED)")
    private String status;  // 결제 상태 (PaymentStatus enum의 name())

    @Column(nullable = false)
    @Comment("결제 처리 시간")
    private LocalDateTime paymentDateTime;  // 결제 시간

    @Column(length = 512)
    @Comment("영수증 URL")
    private String receiptUrl;  // 영수증 URL

    @Column(columnDefinition = "TEXT")
    @Comment("결제 정보 (JSON 형식)")
    private String paymentInfoJson;  // 결제 정보 - JSON으로 저장

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시간")
    private LocalDateTime createdAt;  // 엔티티 생성 시간

    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시간")
    private LocalDateTime updatedAt;  // 엔티티 수정 시간

    // 취소 정보 리스트
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CancelInfoEntity> cancelInfos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
