package com.hunnit_beasts.payment.application.dto.response.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 페이지 정보 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageInfoDto {
    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
}
