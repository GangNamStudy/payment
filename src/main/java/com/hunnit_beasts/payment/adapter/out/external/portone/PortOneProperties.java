package com.hunnit_beasts.payment.adapter.out.external.portone;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("portone.secret")
public class PortOneProperties {
    // api -> apiKey로 변환
    private String api;
    private String webhook;
    private String storeId;
}