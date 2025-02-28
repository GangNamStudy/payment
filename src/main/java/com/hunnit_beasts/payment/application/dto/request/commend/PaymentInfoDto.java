package com.hunnit_beasts.payment.application.dto.request.commend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hunnit_beasts.payment.domain.vo.CardPaymentInfo;
import com.hunnit_beasts.payment.domain.vo.PaymentInfo;
import com.hunnit_beasts.payment.domain.vo.SimplePaymentInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 정보 DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfoDto {
    @NotBlank(message = "결제 정보 타입은 필수입니다")
    private String type;

    // 카드 결제 정보
    private String cardNumber;
    private String expiry;
    private String cvc;
    private Integer installmentMonths;

    // 간편결제 정보
    private String userId;

    /**
     * 도메인 객체 변환을 위한 메서드
     */
    @JsonIgnore
    public PaymentInfo toDomainPaymentInfo() {
        return switch (type.toUpperCase()) {
            case "CARD" -> CardPaymentInfo.of(cardNumber, expiry, cvc, installmentMonths);
            case "KAKAO_PAY" -> SimplePaymentInfo.kakao(userId);
            case "TOSS_PAY" -> SimplePaymentInfo.toss(userId);
            default -> throw new IllegalArgumentException("지원하지 않는 결제 정보 타입입니다: " + type);
        };
    }
}
