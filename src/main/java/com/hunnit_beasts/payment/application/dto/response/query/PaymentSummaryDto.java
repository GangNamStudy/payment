package com.hunnit_beasts.payment.application.dto.response.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 목록 요약 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "결제 정보 요약")
public class PaymentSummaryDto {
    @Schema(description = "결제 ID", example = "PAY-12345")
    private String paymentId;

    @Schema(description = "결제 식별자", example = "ORD-12345")
    private String identifier;

    @Schema(description = "결제 금액(원)", example = "15000")
    private Integer amount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "결제 일시", example = "2023-01-01T12:30:45")
    private LocalDateTime paymentDate;

    @Schema(description = "결제 상태", example = "SUCCESS",
            allowableValues = {"PENDING", "SUCCESS", "FAILED", "PARTIALLY_CANCELED", "CANCELED", "REFUNDED"})
    private String status;

    @Schema(description = "주문명", example = "정액권 결제")
    private String orderName;

    @Schema(description = "결제 수단", example = "CARD",
            allowableValues = {"CARD", "KAKAO_PAY", "TOSS_PAY"})
    private String paymentMethod;
}