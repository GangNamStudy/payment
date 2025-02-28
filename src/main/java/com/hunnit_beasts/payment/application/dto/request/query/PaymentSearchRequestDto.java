package com.hunnit_beasts.payment.application.dto.request.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.enums.SearchType;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 검색 요청 DTO (QueryParam 매핑용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSearchRequestDto {
    private String search;
    private String searchType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    private String status;
    private String paymentMethod;
    private String sortField;
    private String sortDirection;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

    /**
     * 도메인 객체 변환을 위한 메서드
     */
    @JsonIgnore
    public PaymentSearchCriteria toDomain() {
        SearchType domainSearchType = null;
        if (searchType != null) {
            domainSearchType = switch (searchType.toUpperCase()) {
                case "PAYMENT_ID" -> SearchType.PAYMENT_ID;
                case "IDENTIFIER" -> SearchType.IDENTIFIER;
                case "ORDER_NAME" -> SearchType.ORDER_NAME;
                default -> null;
            };
        }

        SortDirection domainSortDirection = null;
        if (sortDirection != null) {
            domainSortDirection = switch (sortDirection.toUpperCase()) {
                case "ASC" -> SortDirection.ASC;
                case "DESC" -> SortDirection.DESC;
                default -> null;
            };
        }

        PaymentStatus domainStatus = null;
        if (status != null) {
            try {
                domainStatus = PaymentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // 유효하지 않은 상태는 무시
            }
        }

        return new PaymentSearchCriteria(
                search,
                domainSearchType,
                startDate,
                endDate,
                domainStatus,
                paymentMethod,
                sortField,
                domainSortDirection,
                page != null ? page : 0,
                size != null ? size : 10
        );
    }
}
