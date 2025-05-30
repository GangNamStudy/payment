package com.hunnit_beasts.payment.application.mapper;

import com.hunnit_beasts.payment.application.dto.response.query.PageInfoDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import com.hunnit_beasts.payment.domain.model.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 결제 조회 DTO 매퍼
 * 도메인 객체를 조회용 DTO로 변환
 */
@Slf4j
@Component
public class PaymentQueryDtoMapper {

    /**
     * Payment 도메인 객체를 PaymentSummaryDto로 변환
     * @param payment 결제 도메인 객체
     * @return 결제 요약 DTO
     */
    public PaymentSummaryDto toSummaryDto(Payment payment) {
        if (payment == null)
            return null;
        return PaymentSummaryDto.builder()
                .paymentId(payment.getId().getValue())
                .identifier(payment.getIdentifier().getValue())
                .amount(payment.getAmount().getAmount().intValue())
                .paymentDate(payment.getStatusInfo().getCreatedAt())
                .status(payment.getStatusInfo().getStatus().name())
                .orderName(payment.getOrderName())
                .paymentMethod(extractPaymentMethod(payment))
                .build();
    }

    /**
     * Payment 페이지를 PaymentListResponseDto로 변환
     * @param paymentPage 페이징된 결제 정보
     * @return 결제 목록 응답 DTO
     */
    public PaymentListResponseDto toListResponseDto(Page<Payment> paymentPage) {
        if (paymentPage == null)
            return PaymentListResponseDto.builder()
                    .payments(List.of())
                    .pageInfo(createEmptyPageInfo())
                    .build();

        List<PaymentSummaryDto> paymentSummaries = paymentPage.getContent()
                .stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());

        PageInfoDto pageInfo = createPageInfo(paymentPage);

        return PaymentListResponseDto.builder()
                .payments(paymentSummaries)
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 페이지 정보 DTO 생성
     * @param page 페이지 객체
     * @return 페이지 정보 DTO
     */
    private PageInfoDto createPageInfo(Page<?> page) {
        return PageInfoDto.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    /**
     * 빈 페이지 정보 생성
     * @return 빈 페이지 정보 DTO
     */
    private PageInfoDto createEmptyPageInfo() {
        return PageInfoDto.builder()
                .currentPage(0)
                .totalPages(0)
                .totalElements(0L)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    /**
     * 결제 수단 정보 추출
     * @param payment 결제 도메인 객체
     * @return 결제 수단 문자열
     */
    private String extractPaymentMethod(Payment payment) {
        if (payment.getProcessingInfo() != null)
            return payment.getProcessingInfo().getPayMethod();
        return "UNKNOWN";
    }
}