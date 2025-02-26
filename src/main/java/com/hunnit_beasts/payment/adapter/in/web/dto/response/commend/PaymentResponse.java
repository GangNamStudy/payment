package com.hunnit_beasts.payment.adapter.in.web.dto.response.commend;

import com.hunnit_beasts.payment.domain.enums.PaymentMethod;
import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {
    /** 결제 ID */
    private Long paymentId;
    /** 결제 식별자 */
    private String identifier;
    /** 결제 시간 */
    private LocalDateTime paymentDateTime;
    /** 결제 금액 */
    private BigDecimal amount;
    /** 결제 수단 */
    private PaymentMethod paymentMethod;
    /** 결제 상태 */
    private PaymentStatus status;
    /** 주문명 */
    private String orderName;
    /** 영수증 URL */
    private String receiptUrl;
}
