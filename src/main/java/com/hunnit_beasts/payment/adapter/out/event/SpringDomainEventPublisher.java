package com.hunnit_beasts.payment.adapter.out.event;

import com.hunnit_beasts.payment.domain.event.DomainEvent;
import com.hunnit_beasts.payment.domain.event.payment.PaymentEvent;
import com.hunnit_beasts.payment.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 스프링 이벤트 시스템을 사용한 도메인 이벤트 발행자 구현체
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        if (event == null) {
            log.warn("발행할 이벤트가 null입니다");
            return;
        }

        if (event instanceof PaymentEvent) {
            PaymentEvent paymentEvent = (PaymentEvent) event;
            log.info("도메인 이벤트 발행: 타입={}, 결제ID={}, 발생시간={}",
                    event.getEventType(),
                    paymentEvent.getPaymentId().getValue(),
                    event.getOccurredAt());
        } else {
            log.info("도메인 이벤트 발행: 타입={}, 발생시간={}",
                    event.getEventType(),
                    event.getOccurredAt());
        }

        applicationEventPublisher.publishEvent(event);
    }
}