package com.hunnit_beasts.payment.adapter.out.external.portone;

import io.portone.sdk.server.errors.WebhookVerificationException;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PortOneWebhookVerifier implements com.hunnit_beasts.payment.port.out.WebhookVerifier {

    private final WebhookVerifier portoneWebhook;

    public PortOneWebhookVerifier(PortOneProperties properties) {
        this.portoneWebhook = new WebhookVerifier(properties.getWebhook());
    }

    @Override
    public Object verify(String body, String webhookId, String webhookTimestamp, String webhookSignature) throws WebhookVerificationException {
        return portoneWebhook.verify(body, webhookId, webhookTimestamp, webhookSignature);
    }
}