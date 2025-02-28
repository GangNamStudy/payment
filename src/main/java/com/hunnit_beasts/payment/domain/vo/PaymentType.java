package com.hunnit_beasts.payment.domain.vo;

/**
 * 결제 유형 전략 인터페이스 (전략 패턴)
 * 각 결제 수단별로 처리 로직을 캡슐화하여 확장성 개선
 */
public interface PaymentType {
    /**
     * 결제 수단 코드
     */
    String getCode();

    /**
     * 결제 수단 설명
     */
    String getDescription();

    /**
     * 결제 정보 유효성 검증
     */
    void validate(PaymentInfo paymentInfo);

    /**
     * 결제 요청 처리 가능 여부 확인
     */
    boolean canProcess(PaymentInfo paymentInfo);
}
