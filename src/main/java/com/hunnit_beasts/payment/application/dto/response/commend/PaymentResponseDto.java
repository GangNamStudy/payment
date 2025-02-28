package com.hunnit_beasts.payment.application.dto.response.commend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PaymentResponseDto {
    private String paymentId;
    private String identifier;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentDateTime;

    private Integer amount;
    private String paymentMethod;
    private String status;
    private String orderName;
    private String receiptUrl;
}
