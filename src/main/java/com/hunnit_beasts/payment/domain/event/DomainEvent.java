package com.hunnit_beasts.payment.domain.event;

import java.time.LocalDateTime;

/**
 * 도메인 이벤트 마커 인터페이스
 */
public interface DomainEvent {
    LocalDateTime getOccurredAt();
}
