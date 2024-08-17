package com.SAGA_order.order.service;

import com.SAGA_order.order.DeliveryMessage;
import com.SAGA_order.order.domain.Order;
import com.SAGA_order.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.product}")
    private String productQueue;

    //TODO : Map -> DB
    private Map<UUID,Order> orderStore = new HashMap<>();

    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRequestDto.toOrder();
        // Order를 DB에 저장
        orderStore.put(order.getOrderId(), order);

        DeliveryMessage deliveryMessage = orderRequestDto.toDeliverymessage(order.getOrderId());
        rabbitTemplate.convertAndSend(productQueue,deliveryMessage);

        return order;
    }

    public Order getOrder(UUID orderId) {
        return orderStore.get(orderId);
    }

    public void rollbackOrder(DeliveryMessage deliveryMessage) {
        Order order = orderStore.get(deliveryMessage.getOrderID());
        order.cancleOrder(deliveryMessage.getErrorType()); //에러 타입 기록
    }
}
