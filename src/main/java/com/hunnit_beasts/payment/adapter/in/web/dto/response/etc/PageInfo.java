package com.hunnit_beasts.payment.adapter.in.web.dto.response.etc;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
    /** 현재 페이지 */
    private int currentPage;
    /** 전체 페이지 수 */
    private int totalPages;
    /** 전체 요소 수 */
    private long totalElements;
    /** 다음 페이지 존재 여부 */
    private boolean hasNext;
    /** 이전 페이지 존재 여부 */
    private boolean hasPrevious;
}
