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
 * 결제 취소 응답 DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "결제 취소 응답 정보")
public class PaymentCancelResponseDto {
    @Schema(description = "결제 ID", example = "PAY-12345")
    private String paymentId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "취소 일시", example = "2023-01-02T14:30:45")
    private LocalDateTime cancelDateTime;

    @Schema(description = "취소 금액(원)", example = "15000")
    private Integer cancelAmount;

    @Schema(description = "결제 상태", example = "CANCELED",
            allowableValues = {"PARTIALLY_CANCELED", "CANCELED", "REFUNDED"})
    private String status;

    @Schema(description = "원 결제 정보")
    private OriginalPaymentInfoDto originalPaymentInfo;
}