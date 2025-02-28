package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.application.dto.request.query.PaymentSearchRequestDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentListResponseDto;
import com.hunnit_beasts.payment.application.dto.response.query.PaymentSummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentQueryAdapter {

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentSummaryDto> getPayment(@PathVariable Long paymentId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping
    public ResponseEntity<PaymentListResponseDto> getPayments(PaymentSearchRequestDto criteria) {
        throw new UnsupportedOperationException();
    }
}
