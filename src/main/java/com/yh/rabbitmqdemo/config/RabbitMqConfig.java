package com.yh.rabbitmqdemo.config;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


/**
 * rabbitmq 配置
 *
 * @author : yh
 * @date : 2021/9/9 20:12
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @DependsOn("messageConverter")
    @Bean("rabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate(rabbitTemplate);
        return rabbitTemplate;
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /*
     *定制RabbitTemplate
     * 1.服务收到消息就回调
     *    1）spring.rabbitmq.publisher-confirms=true
     *    2)设置确认回调confirmCallback
     * 2.消息正确抵达队列进行回调
     *    1）spring.rabbitmq.publisher-returns=true
     *    2) spring.rabbitmq.template.mandatory=true
     *    设置确认回调ReturnCallback
     * 3.消费端确认（保证每个消费被正确消费，此时才可以broker删除这个消息）
     *    1）默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
     *       问题：
     *           我们收到很多消息，自动回复服务器ack，只有一个消息处理成功，宕机了，就会发生消息丢失。
     *           消费者手动确认模式，只要我们没有明确告诉MQ，货物被签收，没有ACK
     *           消息就一直是unacked状态，即使Consumer宕机。消息不会丢失，会重新变成ready
     *   2)如何签收：
     *      channel.basicAck(deliveryTag,false);签收获取
     *      channel.basicNack(deliveryTag,false,true);拒签
     *
     */

    private void initRabbitTemplate(RabbitTemplate rabbitTemplate) {
        //Publisher -> Broker
        RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
            /**
             * 1.做好消息确认机制
             * 2.每一个发送消息都在数据库做好记录，定期将失败的消息再发送一遍
             * @param correlationData 回调信息
             * @param ack   是否成功
             * @param cause 原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack) {
                    log.error("发送失败->{},case->{}", JSONObject.toJSONString(correlationData), cause);
                }
            }
        };
        rabbitTemplate.setConfirmCallback(confirmCallback);
        /**
         * 消息失败返回   Exchange  ->  Queue
         * (例如路由至队列失败)
         */
        RabbitTemplate.ReturnsCallback returnsCallback = returnedMessage -> log.error(JSONObject.toJSONString(returnedMessage));
        rabbitTemplate.setReturnsCallback(returnsCallback);
    }
}
