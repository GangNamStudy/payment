package com.hunnit_beasts.payment.adapter.in.web.dto.request.commend;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CancelRequest {
    /** 취소 사유 */
    private String reason;
    /** 취소 금액 */
    private BigDecimal cancelAmount;
}
