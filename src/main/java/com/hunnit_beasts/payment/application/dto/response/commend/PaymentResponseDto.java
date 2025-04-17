package com.hunnit_beasts.payment.application.dto.response.commend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 응답 DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "결제 응답 정보")
public class PaymentResponseDto {
    @Schema(description = "결제 ID", example = "PAY-12345")
    private String paymentId;

    @Schema(description = "결제 식별자", example = "ORD-12345")
    private String identifier;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "결제 일시", example = "2023-01-01T12:30:45")
    private LocalDateTime paymentDateTime;

    @Schema(description = "결제 금액(원)", example = "15000")
    private Integer amount;

    @Schema(description = "결제 수단", example = "CARD",
            allowableValues = {"CARD", "KAKAO_PAY", "TOSS_PAY"})
    private String paymentMethod;

    @Schema(description = "결제 상태", example = "COMPLETED",
            allowableValues = {"PENDING", "COMPLETED", "FAILED", "CANCELED"})
    private String status;

    @Schema(description = "주문명", example = "정액권 결제")
    private String orderName;

    @Schema(description = "영수증 URL", example = "https://example.com/receipt/12345")
    private String receiptUrl;
}