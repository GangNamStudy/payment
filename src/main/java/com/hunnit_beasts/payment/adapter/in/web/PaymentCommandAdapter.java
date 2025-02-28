package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.application.dto.request.commend.PaymentCancelRequestDto;
import com.hunnit_beasts.payment.application.dto.request.commend.PaymentRequestDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentCancelResponseDto;
import com.hunnit_beasts.payment.application.dto.response.commend.PaymentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentCommandAdapter {
    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody PaymentRequestDto request) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentCancelResponseDto> cancelPayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentCancelRequestDto request) {
        throw new UnsupportedOperationException();
    }
}
