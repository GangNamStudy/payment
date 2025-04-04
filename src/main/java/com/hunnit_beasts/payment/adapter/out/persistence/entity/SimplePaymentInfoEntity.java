package com.hunnit_beasts.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 간편결제 정보 엔티티
 */
@Entity
@Table(
        name = "simple_payment_infos",
        indexes = {
                @Index(name = "idx_simple_payment_infos_type", columnList = "simple_payment_type"),
                @Index(name = "idx_simple_payment_infos_user_id", columnList = "user_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplePaymentInfoEntity {

    @Id
    @Column(length = 36)
    @Comment("결제 ID (PaymentEntity의 ID와 동일)")
    private String paymentId;  // PaymentEntity의 ID와 1:1 매핑

    @JoinColumn(name = "payment_id")
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private PaymentEntity payment;

    @Column(nullable = false, length = 20, name = "simple_payment_type")
    @Comment("간편결제 유형 (KAKAO_PAY, TOSS_PAY 등)")
    private String simplePaymentType;  // 간편결제 유형 (KAKAO_PAY, TOSS_PAY 등)

    @Column(nullable = false, length = 100, name = "user_id")
    @Comment("사용자 ID")
    private String userId;  // 사용자 ID

    @Column(length = 50)
    @Comment("간편결제 서비스 내 결제 수단 식별자")
    private String paymentMethodKey;  // 간편결제 서비스 내 결제 수단 식별자

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시간")
    private LocalDateTime createdAt;  // 엔티티 생성 시간

    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시간")
    private LocalDateTime updatedAt;  // 엔티티 수정 시간

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
