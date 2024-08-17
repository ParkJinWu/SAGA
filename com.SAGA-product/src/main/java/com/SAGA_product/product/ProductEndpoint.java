package com.SAGA_product.product;

import com.SAGA_product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEndpoint {
    private final ProductService productService;

    @RabbitListener(queues = "${message.queue.product}")
    public void receiveMessage(DeliveryMessage deliveryMessage) {
        // 객체 내용을 toString() 메서드로 변환
        log.info("PRODUCT RECEIVE: {}", deliveryMessage.toString());
        // deliveryMessage를 market.payment로 전달
        productService.reduceProductAmount(deliveryMessage);
    }

    @RabbitListener(queues = "${message.queue.err.product}")
    public void receiveErrorMessage(DeliveryMessage deliveryMessage) {
        log.info("ERROR RECEIVE !!!");

        productService.rollbackProduct(deliveryMessage);
    }
}
