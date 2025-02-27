package com.hunnit_beasts.payment.application.dto.response.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 결제 목록 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListResponseDto {
    private List<PaymentSummaryDto> payments;
    private PageInfoDto pageInfo;
}
