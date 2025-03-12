package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.application.dto.request.query.PaymentSearchRequestDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentQueryAdapter {

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentSummaryDto> getPayment(@PathVariable Long paymentId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    @Operation(
            summary = "결제 목록 조회",
            description = "다양한 검색 조건을 적용하여 결제 목록을 조회합니다."
    )
    public ResponseEntity<PaymentListResponseDto> getPaymentsWithQueryParams(
            @Parameter(description = "검색어", example = "ORD-12345")
            @RequestParam(required = false) String search,

            @Parameter(description = "검색 유형 (PAYMENT_ID, IDENTIFIER, ORDER_NAME)", example = "ORDER_NAME")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색 시작일", example = "2023-01-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,

            @Parameter(description = "검색 종료일", example = "2023-12-31T23:59:59")
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate,

            @Parameter(description = "결제 상태 (PENDING, COMPLETED, FAILED, CANCELED)",
                    example = "COMPLETED",
                    schema = @Schema(allowableValues = {"PENDING", "COMPLETED", "FAILED", "CANCELED"}))
            @RequestParam(required = false) String status,

            @Parameter(description = "결제 방법 (CARD, BANK_TRANSFER, VIRTUAL_ACCOUNT)",
                    example = "CARD",
                    schema = @Schema(allowableValues = {"CARD", "BANK_TRANSFER", "VIRTUAL_ACCOUNT"}))
            @RequestParam(required = false) String paymentMethod,

            @Parameter(description = "정렬 필드 (createdAt, amount, orderName)", example = "createdAt")
            @RequestParam(required = false) String sortField,

            @Parameter(description = "정렬 방향 (ASC, DESC)",
                    example = "DESC",
                    schema = @Schema(allowableValues = {"ASC", "DESC"}))
            @RequestParam(required = false) String sortDirection,

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {

        PaymentSearchRequestDto criteria = PaymentSearchRequestDto.builder()
                .search(search)
                .searchType(searchType)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .paymentMethod(paymentMethod)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();

        throw new UnsupportedOperationException();
    }
}
