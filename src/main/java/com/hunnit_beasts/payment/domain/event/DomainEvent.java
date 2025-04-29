package com.hunnit_beasts.payment.domain.event;

import java.time.LocalDateTime;

/**
 * 도메인 이벤트 마커 인터페이스
 */
public interface DomainEvent {
    /**
     * 이벤트 발생 시간 반환
     */
    LocalDateTime getOccurredAt();

    /**
     * 이벤트 유형 반환
     */
    String getEventType();
}
