package com.hunnit_beasts.payment.domain.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 도메인 이벤트를 수집하고 발행하는 역할 담당
 * 헥사고날 아키텍처에서는 애플리케이션 계층에서 이 레지스트리를 통해 이벤트를 처리
 */
public class DomainEvents {
    // 스레드 로컬을 사용하여 현재 트랜잭션 컨텍스트에서의 이벤트 추적
    private static final ThreadLocal<List<DomainEvent>> events = ThreadLocal.withInitial(ArrayList::new);

    /**
     * 도메인 이벤트 발행
     */
    public static void publish(DomainEvent event) {
        events.get().add(event);
    }

    /**
     * 현재까지 수집된 모든 이벤트 반환 및 초기화
     */
    public static List<DomainEvent> collectEvents() {
        List<DomainEvent> collectedEvents = new ArrayList<>(events.get());
        events.get().clear();
        return collectedEvents;
    }

    /**
     * 모든 이벤트 초기화
     */
    public static void clear() {
        events.get().clear();
    }

    /**
     * 스레드 로컬 리소스 정리
     */
    public static void remove() {
        events.remove();
    }
}
