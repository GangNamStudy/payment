package com.hunnit_beasts.payment.adapter.in.web.dto.response.commend;

import com.hunnit_beasts.payment.adapter.in.web.dto.response.etc.OriginalPaymentInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CancelResponse {
    private Long paymentId;
    private String cancelDateTime;
    private BigDecimal cancelAmount;
    private String status;
    private OriginalPaymentInfo originalPaymentInfo;
}
