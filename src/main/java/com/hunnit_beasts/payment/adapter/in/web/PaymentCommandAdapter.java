package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.request.commend.PaymentRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment Command API", description = "결제 생성 및 취소 API")
public class PaymentCommandAdapter {

    @PostMapping
    @Operation(
            summary = "결제 생성",
            description = "새로운 결제를 생성합니다. 결제 정보와 결제 수단을 포함한 요청으로 결제를 처리합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PaymentResponseDto> createPayment(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결제 요청 정보",
                    content = @Content(schema = @Schema(implementation = PaymentRequestDto.class))
            )
            PaymentRequestDto request) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{paymentId}/cancel")
    @Operation(
            summary = "결제 취소",
            description = "기존 결제를 취소합니다. 취소 사유와 취소 금액을 포함한 요청으로 결제를 취소합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 취소 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentCancelResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<PaymentCancelResponseDto> cancelPayment(
            @Parameter(description = "취소할 결제 ID", required = true, example = "PAY-12345")
            @PathVariable Long paymentId,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결제 취소 요청 정보",
                    content = @Content(schema = @Schema(implementation = PaymentCancelRequestDto.class))
            )
            PaymentCancelRequestDto request) {
        throw new UnsupportedOperationException();
    }
}