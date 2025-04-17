package com.hunnit_beasts.payment.application.dto.response.commend;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 원본 결제 정보 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "원본 결제 정보")
public class OriginalPaymentInfoDto {
    @Schema(description = "결제 ID", example = "PAY-12345")
    private String paymentId;

    @Schema(description = "결제 금액(원)", example = "15000")
    private Integer amount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "결제 일시", example = "2023-01-01T12:30:45")
    private LocalDateTime paymentDate;
}