package com.hunnit_beasts.payment.port.out;

import com.hunnit_beasts.payment.domain.Payment;
import com.hunnit_beasts.payment.domain.vo.Money;
import com.hunnit_beasts.payment.domain.vo.PaymentType;
import com.hunnit_beasts.payment.port.out.dto.PaymentCancelResult;
import com.hunnit_beasts.payment.port.out.dto.PaymentProcessResult;

/**
 * 결제 프로세서 포트 (출력 포트)
 * 도메인에서 외부 결제 시스템에 접근하기 위한 인터페이스
 */
public interface PaymentProcessor {
    /**
     * 결제 승인 요청
     */
    PaymentProcessResult requestPayment(Payment payment);

    /**
     * 결제 취소 요청
     */
    PaymentCancelResult requestCancel(Payment payment, Money cancelAmount);

    /**
     * 결제 수단이 지원되는지 확인
     */
    boolean supports(PaymentType paymentType);
}
