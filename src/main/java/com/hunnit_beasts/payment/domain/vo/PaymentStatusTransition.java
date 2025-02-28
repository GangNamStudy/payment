package com.hunnit_beasts.payment.domain.vo;

import com.hunnit_beasts.payment.domain.enums.PaymentStatus;

/**
 * 결제 상태 전이 검증 클래스 (상태 기계 패턴)
 */
public class PaymentStatusTransition {
    public static void validate(PaymentStatus current, PaymentStatus next) {
        if (!canTransition(current, next)) {
            throw new IllegalStateException(
                    String.format("결제 상태를 %s에서 %s로 변경할 수 없습니다",
                            current, next));
        }
    }

    private static boolean canTransition(PaymentStatus current, PaymentStatus next) {
        return switch (current) {
            case PENDING -> next == PaymentStatus.SUCCESS || next == PaymentStatus.FAILED;
            case SUCCESS -> next == PaymentStatus.CANCELED ||
                    next == PaymentStatus.PARTIALLY_CANCELED ||
                    next == PaymentStatus.REFUNDED;
            case PARTIALLY_CANCELED -> next == PaymentStatus.CANCELED ||
                    next == PaymentStatus.REFUNDED;
            case CANCELED -> next == PaymentStatus.REFUNDED;
            default -> false;
        };
    }
}
