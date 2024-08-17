package com.SAGA_order.order;

import com.SAGA_order.order.domain.Order;
import com.SAGA_order.order.dto.OrderRequestDto;
import com.SAGA_order.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderEndpoint {

    @RabbitListener(queues = "${message.queue.err.order}")
    public void errOrder(DeliveryMessage deliveryMessage){
        log.info("ERROR RECEIVE !!!");
        orderService.rollbackOrder(deliveryMessage);
    }

    private final OrderService orderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/order")
    public ResponseEntity<Order> order(@RequestBody OrderRequestDto orderRequestDtoorder) {
        Order order = orderService.createOrder(orderRequestDtoorder);
        return ResponseEntity.ok(order);
    }



}
