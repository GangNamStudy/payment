package com.hunnit_beasts.payment.domain.model.method;

import java.util.Map;

public interface PaymentMethod {
    /**
     * 결제 수단 코드 반환
     */
    String getMethodCode();

    /**
     * 결제 수단 이름 반환
     */
    String getMethodName();

    /**
     * 결제 수단 유효성 검증
     */
    void validate();

    /**
     * 결제 처리에 필요한 파라미터 반환
     */
    Map<String, Object> getProcessingParameters();

    /**
     * 결제 수단 정보의 민감 정보가 제거된 정보 반환
     */
    Map<String, Object> getMaskedInfo();
}
