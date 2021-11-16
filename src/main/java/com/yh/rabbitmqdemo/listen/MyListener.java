package com.yh.rabbitmqdemo.listen;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.yh.rabbitmqdemo.domin.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : yh
 * @date : 2021/9/9 21:31
 */
@Component
@Slf4j
@RabbitListener(queues = "my.simple.queue")
public class MyListener {

    @SneakyThrows
    @RabbitHandler
    public void handler(User user, Message message, Channel channel) {
        String res = JSONObject.toJSONString(user);
        try {
            //是否重复消费
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            if (redelivered) {
                log.error("重复消费->{}", res);
                return;
            }

            // 消费消息...
            log.info("消费消息成功->{}", res);

            //手动确认消息送达已消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消费消息失败->{}", res);
            //消费失败，打回去
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            throw e;
        }
    }

    @SneakyThrows
    @RabbitHandler
    public void handler(String res, Message message, Channel channel) {
        try {
            //是否重复消费
            Boolean redelivered = message.getMessageProperties().getRedelivered();
            if (redelivered) {
                log.error("重复消费->{}", res);
                return;
            }

            // 消费消息...
            log.info("消费延时消息成功->{}", res);

            //手动确认消息送达已消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消费延时消息成功->{}", res);
            //消费失败，打回去
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            throw e;
        }
    }

}
