package com.hunnit_beasts.payment.application.dto.request.commend;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 취소 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequestDto {
    @NotBlank(message = "취소 사유는 필수입니다")
    private String reason;

    @NotNull(message = "취소 금액은 필수입니다")
    @Min(value = 1, message = "취소 금액은 1 이상이어야 합니다")
    private Integer cancelAmount;
}
