package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class Receipt {
    private final String url;

    private Receipt(String url) {
        if (url == null || url.trim().isEmpty())
            throw new IllegalArgumentException("영수증 URL은 null이거나 빈 값일 수 없습니다");
        if (!isValidUrl(url))
            throw new IllegalArgumentException("유효하지 않은 URL 형식입니다: " + url);
        this.url = url;
    }

    private boolean isValidUrl(String url) {
        // 간단한 URL 검증 로직 (실제로는 정규식이나 URL 클래스 사용 권장)
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static Receipt of(String url) {
        return new Receipt(url);
    }
}
