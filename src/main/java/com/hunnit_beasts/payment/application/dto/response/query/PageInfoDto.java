package com.hunnit_beasts.payment.application.dto.response.query;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "페이지 정보")
public class PageInfoDto {
    @Schema(description = "현재 페이지 번호", example = "0")
    private Integer currentPage;

    @Schema(description = "전체 페이지 수", example = "5")
    private Integer totalPages;

    @Schema(description = "전체 항목 수", example = "42")
    private Long totalElements;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    @Schema(description = "이전 페이지 존재 여부", example = "false")
    private boolean hasPrevious;
}