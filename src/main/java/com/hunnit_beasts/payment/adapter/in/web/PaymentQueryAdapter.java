package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.application.dto.request.query.PaymentSearchRequestDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment Query API", description = "결제 조회 API")
public class PaymentQueryAdapter {

    @GetMapping("/{paymentId}")
    @Operation(
            summary = "결제 상세 조회",
            description = "결제 ID를 기반으로 특정 결제의 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 정보 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentSummaryDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PaymentSummaryDto> getPayment(
            @Parameter(description = "조회할 결제 ID", required = true, example = "1234")
            @PathVariable Long paymentId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    @Operation(
            summary = "결제 목록 조회",
            description = "다양한 검색 조건을 적용하여 결제 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 목록 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentListResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PaymentListResponseDto> getPaymentsWithQueryParams(
            @Parameter(description = "검색어", example = "ORD-12345")
            @RequestParam(required = false) String search,

            @Parameter(description = "검색 유형 (PAYMENT_ID, IDENTIFIER, ORDER_NAME)", example = "ORDER_NAME",
                    schema = @Schema(allowableValues = {"PAYMENT_ID", "IDENTIFIER", "ORDER_NAME"}))
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

            @Parameter(description = "결제 방법 (CARD, KAKAO_PAY, TOSS_PAY)",
                    example = "CARD",
                    schema = @Schema(allowableValues = {"CARD", "KAKAO_PAY", "TOSS_PAY"}))
            @RequestParam(required = false) String paymentMethod,

            @Parameter(description = "정렬 필드 (createdAt, amount, orderName)", example = "createdAt",
                    schema = @Schema(allowableValues = {"createdAt", "amount", "orderName"}))
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