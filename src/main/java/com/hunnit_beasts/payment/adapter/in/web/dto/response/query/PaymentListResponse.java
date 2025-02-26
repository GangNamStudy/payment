package com.hunnit_beasts.payment.adapter.in.web.dto.response.query;

import com.hunnit_beasts.payment.adapter.in.web.dto.response.commend.PaymentResponse;
import com.hunnit_beasts.payment.adapter.in.web.dto.response.etc.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PaymentListResponse {
    /** 결제 목록 */
    private List<PaymentResponse> payments;
    /** 페이징 정보 */
    private PageInfo pageInfo;
}
