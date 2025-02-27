package com.hunnit_beasts.payment.port.in.dto;

import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.enums.SearchType;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 검색 조건 값 객체
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentSearchCriteria {
    private String searchText;
    private SearchType searchType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PaymentStatus status;
    private String paymentType;
    private String sortField;
    private SortDirection sortDirection;
    private int page;
    private int size;
}
