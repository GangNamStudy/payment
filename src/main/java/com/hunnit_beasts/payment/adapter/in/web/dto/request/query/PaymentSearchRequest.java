package com.hunnit_beasts.payment.adapter.in.web.dto.request.query;

import com.hunnit_beasts.payment.domain.enums.PaymentMethod;
import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.enums.SearchType;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentSearchRequest {
    /** 검색어 */
    private String search;
    /** 검색 유형 */
    private SearchType searchType;
    /** 시작일 */
    private LocalDateTime startDate;
    /** 종료일 */
    private LocalDateTime endDate;
    /** 결제 상태 */
    private PaymentStatus status;
    /** 결제 수단 */
    private PaymentMethod paymentMethod;
    /** 정렬 필드 */
    private String sortField;
    /** 정렬 방향 */
    private SortDirection sortDirection;
    /** 페이지 번호 */
    private Integer page;
    /** 페이지 크기 */
    private Integer size;
}
