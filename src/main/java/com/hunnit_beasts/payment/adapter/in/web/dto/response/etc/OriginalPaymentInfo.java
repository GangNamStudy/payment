package com.hunnit_beasts.payment.adapter.in.web.dto.response.etc;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OriginalPaymentInfo {
    private Long paymentId;
    private BigDecimal amount;
    private String paymentDate;
}
