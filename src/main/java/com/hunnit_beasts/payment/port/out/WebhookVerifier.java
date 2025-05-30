package com.hunnit_beasts.payment.port.out;

import io.portone.sdk.server.errors.WebhookVerificationException;

/**
 * 웹훅 검증 인터페이스
 */
public interface WebhookVerifier {

    /**
     * 웹훅 서명 검증
     * @param body 웹훅 본문
     * @param webhookId 웹훅 ID
     * @param webhookTimestamp 웹훅 타임스탬프
     * @param webhookSignature 웹훅 서명
     * @return 검증된 웹훅 정보
     */
    Object verify(String body, String webhookId, String webhookTimestamp, String webhookSignature) throws WebhookVerificationException;
}