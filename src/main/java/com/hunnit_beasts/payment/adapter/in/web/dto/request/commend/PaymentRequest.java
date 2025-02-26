package com.hunnit_beasts.payment.adapter.in.web.dto.request.commend;

import com.hunnit_beasts.payment.adapter.in.web.dto.request.etc.paymentinfos.PaymentInfo;
import com.hunnit_beasts.payment.domain.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentRequest {
    /** 결제 식별자 */
    private String identifier;
    /** 주문명 */
    private String orderName;
    /** 결제 금액 */
    private BigDecimal amount;
    /** 결제 수단 */
    private PaymentMethod paymentMethod;
    /** 결제 상세 정보 */
    private PaymentInfo paymentInfo;
}
