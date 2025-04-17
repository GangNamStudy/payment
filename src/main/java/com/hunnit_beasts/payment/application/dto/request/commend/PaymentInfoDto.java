package com.hunnit_beasts.payment.application.dto.request.commend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hunnit_beasts.payment.domain.vo.CardPaymentInfo;
import com.hunnit_beasts.payment.domain.vo.PaymentInfo;
import com.hunnit_beasts.payment.domain.vo.SimplePaymentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "결제 상세 정보")
public class PaymentInfoDto {
    @NotBlank(message = "결제 정보 타입은 필수입니다")
    @Schema(description = "결제 정보 타입", example = "CARD",
            allowableValues = {"CARD", "KAKAO_PAY", "TOSS_PAY"})
    private String type;

    // 카드 결제 정보
    @Schema(description = "카드 번호 (CARD 타입인 경우 필수)", example = "1234-5678-9012-3456")
    private String cardNumber;

    @Schema(description = "카드 유효기간 (CARD 타입인 경우 필수)", example = "12/25")
    private String expiry;

    @Schema(description = "카드 CVC (CARD 타입인 경우 필수)", example = "123")
    private String cvc;

    @Schema(description = "할부 개월 수", example = "0", minimum = "0", maximum = "60")
    private Integer installmentMonths;

    // 간편결제 정보
    @Schema(description = "사용자 ID (간편결제 타입인 경우 필수)", example = "user123")
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