package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 결제 상태 정보 값 객체
 */
@Getter
@ToString
@EqualsAndHashCode
public final class PaymentStatusInfo {
    private final PaymentStatus status;          // 결제 상태
    private final LocalDateTime createdAt;       // 생성 시간
    private final LocalDateTime completedAt;     // 완료 시간
    private final String receiptUrl;             // 영수증 URL

    public PaymentStatusInfo(
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime completedAt,
            String receiptUrl) {

        this.status = Objects.requireNonNull(status, "결제 상태는 null일 수 없습니다");
        this.createdAt = Objects.requireNonNull(createdAt, "생성 시간은 null일 수 없습니다");
        this.completedAt = completedAt;
        this.receiptUrl = receiptUrl;
    }

    /**
     * 새로운 인스턴스 생성 (팩토리 메서드)
     */
    public static PaymentStatusInfo of(
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime completedAt,
            String receiptUrl) {

        return new PaymentStatusInfo(
                status,
                createdAt,
                completedAt,
                receiptUrl
        );
    }

    /**
     * 진행 중 상태 인스턴스 생성
     */
    public static PaymentStatusInfo pending() {
        return new PaymentStatusInfo(
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                null,
                null
        );
    }

    /**
     * 성공 상태로 변경한 새 인스턴스 생성
     */
    public PaymentStatusInfo complete(String receiptUrl) {
        return new PaymentStatusInfo(
                PaymentStatus.SUCCESS,
                this.createdAt,
                LocalDateTime.now(),
                receiptUrl
        );
    }

    /**
     * 실패 상태로 변경한 새 인스턴스 생성
     */
    public PaymentStatusInfo fail() {
        return new PaymentStatusInfo(
                PaymentStatus.FAILED,
                this.createdAt,
                LocalDateTime.now(),
                null
        );
    }
}