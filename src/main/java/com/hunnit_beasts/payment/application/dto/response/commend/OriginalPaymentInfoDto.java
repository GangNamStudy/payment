package com.hunnit_beasts.payment.application.dto.response.commend;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OriginalPaymentInfoDto {
    private String paymentId;
    private Integer amount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentDate;
}
