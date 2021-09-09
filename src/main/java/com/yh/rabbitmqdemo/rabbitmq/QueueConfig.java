package com.yh.rabbitmqdemo.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列配置
 *
 * @author : yh
 * @date : 2021/9/9 20:46
 */
@Configuration
public class QueueConfig {

    private static final int TTL = 30 * 60 * 1000;

    @Bean("mySimpleQueue")
    public Queue mySimpleQueue() {
        return QueueBuilder
                .durable("my.simple.queue")
                .build();
    }

    /**
     * 死信队列
     * 延迟消息接受
     * <p>
     * 例如订单未支付30自动取消
     *
     * @return
     */
    @Bean("myTtlQueue")
    public Queue myTtlQueue() {
        return QueueBuilder
                .durable("my.ttl.queue")
                .deadLetterExchange("my-topic-exchange")
                .deadLetterRoutingKey("my.ttl.key")
                //.ttl(TTL)
                .build();
    }

}
