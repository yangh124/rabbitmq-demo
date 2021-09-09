package com.yh.rabbitmqdemo.rabbitmq;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义交换机
 * <p>
 * durable  消息持久化
 *
 * @author : yh
 * @date : 2021/9/9 20:38
 */
@Configuration
public class ExchangeConfig {

    @Bean("myDirectExchange")
    public Exchange myDirectExchange() {
        return ExchangeBuilder.directExchange("my-direct-exchange").durable(true).build();
    }

    @Bean("myTopicExchange")
    public Exchange myTopicExchange() {
        return ExchangeBuilder.topicExchange("my-topic-exchange").durable(true).build();
    }

    @Bean("myFanoutExchange")
    public Exchange myFanoutExchange() {
        return ExchangeBuilder.fanoutExchange("my-fanout-exchange").durable(true).build();
    }

    @Bean("myFanoutExchange")
    public Exchange myHeadersExchange() {
        return ExchangeBuilder.headersExchange("my-headers-exchange").durable(true).build();
    }
}
