package com.yh.rabbitmqdemo.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : yh
 * @date : 2021/9/9 21:13
 */
@Configuration
public class BindConfig {

    @Bean
    public Binding bindMySimpleQueue(@Qualifier("mySimpleQueue") Queue queue, @Qualifier("myDirectExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my.simple.key").noargs();
    }

    @Bean
    public Binding bindMyTtlQueue(@Qualifier("myTtlQueue") Queue queue, @Qualifier("myDirectExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my.ttl.key").noargs();
    }
}
