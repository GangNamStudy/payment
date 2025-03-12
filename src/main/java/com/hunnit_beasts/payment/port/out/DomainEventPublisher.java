package com.hunnit_beasts.payment.port.out;

import com.hunnit_beasts.payment.domain.event.DomainEvent;

import java.util.List;

/**
 * 이벤트 발행자 포트 (출력 포트)
 * 도메인 이벤트를 외부 시스템에 발행하기 위한 인터페이스
 */
public interface DomainEventPublisher {
    /**
     * 도메인 이벤트 발행
     */
    void publish(DomainEvent event);

    /**
     * 여러 도메인 이벤트 발행
     */
    default void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
