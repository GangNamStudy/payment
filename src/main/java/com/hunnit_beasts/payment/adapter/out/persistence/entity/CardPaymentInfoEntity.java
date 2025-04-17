package com.hunnit_beasts.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 카드 결제 정보 엔티티 (특정 결제 유형에 대한 추가 정보)
 */
@Entity
@Table(
        name = "card_payment_infos",
        indexes = {
                @Index(name = "idx_card_payment_infos_card_provider", columnList = "card_provider")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPaymentInfoEntity {
    @Id
    @Column(length = 36)
    @Comment("결제 ID (PaymentEntity의 ID와 동일)")
    private String paymentId;  // PaymentEntity의 ID와 1:1 매핑

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @MapsId
    private PaymentEntity payment;

    @Column(nullable = false, length = 25)
    @Comment("마스킹된 카드 번호")
    private String maskedCardNumber;  // 마스킹된 카드 번호 (보안상 실제 카드 번호는 저장하지 않음)

    @Column(nullable = false, length = 50)
    @Comment("카드 발급사")
    private String cardProvider;  // 카드 발급사

    @Column
    @Comment("할부 개월 수")
    private Integer installmentMonths;  // 할부 개월 수

    @Column(length = 20)
    @Comment("카드 종류 (신용, 체크)")
    private String cardType;  // 카드 종류 (신용, 체크)

    @Column(length = 6)
    @Comment("카드 번호 앞 6자리 (BIN)")
    private String cardBin;  // 카드 BIN 번호

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
