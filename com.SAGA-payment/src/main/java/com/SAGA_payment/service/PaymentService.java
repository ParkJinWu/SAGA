package com.SAGA_payment.service;

import com.SAGA_payment.DeliveryMessage;
import com.SAGA_payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.err.product}")
    private String productErrorQueue;

    public void creatPayment(DeliveryMessage deliveryMessage) {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .payAmount(deliveryMessage.getPayAmount())
                .userId(deliveryMessage.getUserId())
                .payStatus("SUCCESS!!!")
                .build();

        if(payment.getPayAmount() >= 10000){
            log.error("Payment amount exceeds limit : {}", payment.getPayAmount());
            // Error Type 기록
            deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEEDS"); // 금액 초과

            // Error Type을 에러 큐에 보낸다.
            this.rollbackPayment(deliveryMessage);
        }
    }

    // Error Queue(market.err.product)로 보내는 메서드
    public void rollbackPayment(DeliveryMessage deliveryMessage) {
        log.info("PAYMENT ROLLBACK!!!!!!!");
        rabbitTemplate.convertAndSend(productErrorQueue,deliveryMessage);
    }
}
