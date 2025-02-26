package com.hunnit_beasts.payment.adapter.in.web;

import com.hunnit_beasts.payment.adapter.in.web.dto.request.commend.CancelRequest;
import com.hunnit_beasts.payment.adapter.in.web.dto.request.commend.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentCommandAdapter {
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long paymentId, @RequestBody CancelRequest request) {
        throw new UnsupportedOperationException();
    }
}
