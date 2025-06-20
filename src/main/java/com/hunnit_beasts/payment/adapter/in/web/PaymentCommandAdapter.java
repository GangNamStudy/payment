package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.adapter.in.web.dto.request.CompletePaymentRequest;
import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import com.hunnit_beasts.payment.port.in.PaymentCommendUseCase;
import com.hunnit_beasts.payment.port.in.PaymentWebhookUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment Command API", description = "결제 생성 및 취소 API")
@RequiredArgsConstructor
public class PaymentCommandAdapter {

    private final PaymentCommendUseCase paymentCommendUseCase;
    private final PaymentWebhookUseCase paymentWebhookUseCase;

    @PostMapping("/complete")
    @Operation(
            summary = "결제 완료",
            description = "포트원 결제 ID를 사용하여 결제를 완료합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 완료 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public CompletableFuture<ResponseEntity<PaymentResponseDto>> completePayment(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결제 완료 요청 정보",
                    content = @Content(schema = @Schema(implementation = CompletePaymentRequest.class))
            )
            CompletePaymentRequest request) {

        return paymentCommendUseCase.completePayment(request.getPaymentId())
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/webhook")
    public CompletableFuture<ResponseEntity<Void>> handleWebhook(
            @RequestBody Map<String, Object> webhookData) {

        String impUid = (String) webhookData.get("imp_uid");
        String merchantUid = (String) webhookData.get("merchant_uid");
        String status = (String) webhookData.get("status");

        paymentWebhookUseCase.handleWebhook(impUid,merchantUid,status);

        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
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
            @PathVariable String paymentId,

            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결제 취소 요청 정보",
                    content = @Content(schema = @Schema(implementation = PaymentCancelRequestDto.class))
            )
            PaymentCancelRequestDto request) {

        PaymentCancelResponseDto response = paymentCommendUseCase.cancelPayment(
                paymentId, request);

        return ResponseEntity.ok(response);
    }
}