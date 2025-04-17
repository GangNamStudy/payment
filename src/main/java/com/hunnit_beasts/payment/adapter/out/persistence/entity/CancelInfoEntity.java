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
 * 취소 정보 엔티티
 */
@Entity
@Table(
        name = "payment_cancels",
        indexes = {
                @Index(name = "idx_payment_cancels_payment_id", columnList = "payment_id"),
                @Index(name = "idx_payment_cancels_cancel_date_time", columnList = "cancel_date_time"),
                @Index(name = "idx_payment_cancels_successful", columnList = "successful")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("취소 정보 ID (자동 증가)")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @Comment("연결된 결제 ID")
    private PaymentEntity payment;

    @Column(nullable = false, length = 512)
    @Comment("취소 사유")
    private String reason;  // 취소 사유

    @Column(nullable = false, precision = 19, scale = 2)
    @Comment("취소 금액")
    private BigDecimal cancelAmount;  // 취소 금액

    @Column(nullable = false, length = 3)
    @Comment("통화 코드 (ISO 4217)")
    private String currency;  // 통화 코드

    @Column(nullable = false)
    @Comment("취소 완료 시간")
    private LocalDateTime cancelDateTime;  // 취소 완료 시간

    @Column(nullable = false)
    @Comment("취소 성공 여부")
    private boolean successful;  // 취소 성공 여부

    @Column(length = 50)
    @Comment("취소 결과 코드")
    private String resultCode;  // 취소 결과 코드

    @Column(length = 512)
    @Comment("취소 결과 메시지")
    private String resultMessage;  // 취소 결과 메시지

    @Column(length = 100)
    @Comment("외부 시스템 취소 트랜잭션 ID")
    private String externalTransactionId;  // 외부 시스템의 트랜잭션 ID

    @Column(length = 1000)
    @Comment("취소 실패 시 에러 상세 정보")
    private String errorDetail;  // 취소 실패 시 에러 상세 정보

    @Column(nullable = false)
    @Comment("취소 요청 시간")
    private LocalDateTime requestDateTime;  // 취소 요청 시간

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
