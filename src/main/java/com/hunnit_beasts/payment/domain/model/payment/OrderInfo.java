package com.hunnit_beasts.payment.domain.model.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public final class OrderInfo {
    private final String orderName;

    private OrderInfo(String orderName) {
        if (orderName == null || orderName.trim().isEmpty())
            throw new IllegalArgumentException("주문명은 null이거나 빈 값일 수 없습니다");
        if (orderName.length() > 100)
            throw new IllegalArgumentException("주문명은 100자를 초과할 수 없습니다");
        this.orderName = orderName;
    }

    public static OrderInfo of(String orderName) {
        return new OrderInfo(orderName);
    }
}
