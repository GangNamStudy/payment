package com.hunnit_beasts.payment.application.dto.request.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hunnit_beasts.payment.domain.enums.PaymentStatus;
import com.hunnit_beasts.payment.domain.enums.SearchType;
import com.hunnit_beasts.payment.domain.enums.SortDirection;
import com.hunnit_beasts.payment.port.in.dto.PaymentSearchCriteria;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "결제 검색 요청 정보")
public class PaymentSearchRequestDto {
    @Schema(description = "검색어", example = "ORD-12345")
    private String search;

    @Schema(description = "검색 유형", example = "ORDER_NAME",
            allowableValues = {"PAYMENT_ID", "IDENTIFIER", "ORDER_NAME"})
    private String searchType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "검색 시작일", example = "2023-01-01T00:00:00")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "검색 종료일", example = "2023-12-31T23:59:59")
    private LocalDateTime endDate;

    @Schema(description = "결제 상태", example = "SUCCESS",
            allowableValues = {"PENDING", "SUCCESS", "FAILED", "PARTIALLY_CANCELED", "CANCELED", "REFUNDED"})
    private String status;

    @Schema(description = "결제 방법", example = "CARD",
            allowableValues = {"CARD", "KAKAO_PAY", "TOSS_PAY"})
    private String paymentMethod;

    @Schema(description = "정렬 필드", example = "createdAt",
            allowableValues = {"createdAt", "amount", "orderName"})
    private String sortField;

    @Schema(description = "정렬 방향", example = "DESC",
            allowableValues = {"ASC", "DESC"})
    private String sortDirection;

    @Builder.Default
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
    private Integer page = 0;

    @Builder.Default
    @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
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