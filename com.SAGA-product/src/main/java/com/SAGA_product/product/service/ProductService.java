package com.SAGA_product.product.service;

import com.SAGA_product.product.DeliveryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    @Value("${message.queue.payment}")
    private String paymentQueue;

    @Value("${message.queue.err.order}")
    private String errOrderQueue;

    private final RabbitTemplate rabbitTemplate;

    public void reduceProductAmount(DeliveryMessage deliveryMessage) {
        Integer productId = deliveryMessage.getProductId();
        Integer productQuantity = deliveryMessage.getProductQuantity();

        if(productId != 1 || productQuantity > 1) {
            this.rollbackProduct(deliveryMessage);
        }
        //정상 동작하면 payment application으로 넘겨주기 위해서 market.payment 큐에 넣는다.
        rabbitTemplate.convertAndSend(paymentQueue ,deliveryMessage);
    }

    public void rollbackProduct(DeliveryMessage deliveryMessage) {
        log.info("PRODUCT ROLLBACK!!!");
        if(!StringUtils.hasText(deliveryMessage.getErrorType())){
            deliveryMessage.setErrorType("PRODUCT ERROR!!!!");
        }
        rabbitTemplate.convertAndSend(errOrderQueue ,deliveryMessage);
    }
}
