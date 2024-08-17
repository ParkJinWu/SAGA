package com.SAGA_order.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationQueueConfig {

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.queue.product}")
    private String queueProduct;

    @Value("${message.queue.payment}")
    private String queuePayment;


    @Value("${message.err.exchange}")
    private String exchangeErr;

    @Value("${message.queue.err.order}")
    private String queueErrOrder;

    @Value("${message.queue.err.product}")
    private String queueErrProduct;

    //Exchange 생성
    @Bean public TopicExchange exchange(){return new TopicExchange(exchange);}

    // market.product 생성
    @Bean public Queue queueProduct(){return new Queue(queueProduct);}

    // market.payment 생성
    @Bean public Queue queuePayment(){return new Queue(queuePayment);}

    // binding 생성 (큐의 이름과 동일) : order ➡️ product Queue
    @Bean public Binding bindingProduct(){
        return BindingBuilder.
                bind(queueProduct()).
                to(exchange()).
                with(queueProduct);
    }

    // binding 생성 (큐의 이름과 동일) : product ➡️ payment Queue
    @Bean public Binding bindingPayment() {
        return BindingBuilder.
                bind(queuePayment()).
                to(exchange()).
                with(queuePayment);
    }

    //Error Exchange 생성
    @Bean public  TopicExchange exchangeErr(){return new TopicExchange(exchangeErr);}

    //market.err.order 생성
    @Bean public Queue queueErrOrder(){return new Queue(queueErrOrder);}

    //market.err.product
    @Bean public Queue queueErrProduct(){return new Queue(queueErrProduct);}

    // Error binding 생성 (큐의 이름과 동일) : product ➡️ market.err.order Queue
    @Bean public Binding bindingErrOrder(){
        return BindingBuilder.
                bind(queueErrOrder()).
                to(exchangeErr()).
                with(queueErrOrder);
    }

    // Error binding 생성 (큐의 이름과 동일) : payment ➡️ market.err.product Queue
    @Bean public Binding bindingErrProduct(){
        return BindingBuilder.
                bind(queueErrProduct()).
                to(exchangeErr()).
                with(queueErrProduct);
    }
}
