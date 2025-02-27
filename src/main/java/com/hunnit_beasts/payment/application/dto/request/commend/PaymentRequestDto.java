package com.hunnit_beasts.payment.application.dto.request.commend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hunnit_beasts.payment.domain.vo.CardPaymentType;
import com.hunnit_beasts.payment.domain.vo.Money;
import com.hunnit_beasts.payment.domain.vo.PaymentType;
import com.hunnit_beasts.payment.domain.vo.SimplePaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 결제 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotBlank(message = "결제 식별자는 필수입니다")
    private String identifier;

    @NotBlank(message = "주문명은 필수입니다")
    private String orderName;

    @NotNull(message = "결제 금액은 필수입니다")
    @Min(value = 1, message = "결제 금액은 1 이상이어야 합니다")
    private Integer amount;

    @NotBlank(message = "결제 수단은 필수입니다")
    private String paymentMethod;

    @NotNull(message = "결제 정보는 필수입니다")
    @Valid
    private PaymentInfoDto paymentInfo;

    /**
     * 도메인 객체 변환을 위한 메서드
     */
    @JsonIgnore
    public PaymentType toPaymentType() {
        return switch (paymentMethod.toUpperCase()) {
            case "CARD" -> CardPaymentType.getInstance();
            case "KAKAO_PAY" -> SimplePaymentType.kakao();
            case "TOSS_PAY" -> SimplePaymentType.toss();
            default -> throw new IllegalArgumentException("지원하지 않는 결제 수단입니다: " + paymentMethod);
        };
    }

    @JsonIgnore
    public Money toMoney() {
        return Money.won(amount);
    }
}
