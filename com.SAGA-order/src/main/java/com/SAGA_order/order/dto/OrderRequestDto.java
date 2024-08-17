package com.SAGA_order.order.dto;


import com.SAGA_order.order.DeliveryMessage;
import com.SAGA_order.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderRequestDto {

    private String userId;
    private Integer productId;
    private Integer productQuantity;
    private Integer payAmount;

    public Order toOrder() {
        return Order.builder().
                orderId(UUID.randomUUID()).
                userId(userId).
                orderStatus("RECEIPT").
                build();
    }

    public DeliveryMessage toDeliverymessage(UUID orderId) {
        return DeliveryMessage.builder()
                .orderID(orderId)
                .userId(userId)
                .productId(productId)
                .productQuantity(productQuantity)
                .payAmount(payAmount).build();
    }
}
