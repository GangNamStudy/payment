package com.hunnit_beasts.payment.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "결제 완료 요청 정보")
public class CompletePaymentRequest {

    @NotBlank(message = "결제 ID는 필수입니다")
    @Schema(description = "결제 ID", example = "imp_123456789")
    private String paymentId;
}