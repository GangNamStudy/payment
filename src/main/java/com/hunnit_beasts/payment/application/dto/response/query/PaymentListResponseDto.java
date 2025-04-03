package com.hunnit_beasts.payment.application.dto.response.query;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "결제 목록 응답 정보")
public class PaymentListResponseDto {
    @Schema(description = "결제 목록")
    private List<PaymentSummaryDto> payments;

    @Schema(description = "페이지 정보")
    private PageInfoDto pageInfo;
}