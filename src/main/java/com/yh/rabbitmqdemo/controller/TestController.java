package com.yh.rabbitmqdemo.controller;

import com.yh.rabbitmqdemo.domin.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : yh
 * @date : 2021/9/9 21:21
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendMsg() {
        User user = new User(1L, "杨皓", "哈哈哈哈哈哈哈哈哈哈哈!");
        rabbitTemplate.convertAndSend("my-direct-exchange", "my.simple.key", user);
        return "success";
    }

    @GetMapping("/sendttl")
    public String sendTtlMsg() {
        int ttl = 60 * 1000;
        rabbitTemplate.convertAndSend("my-direct-exchange", "my.ttl.key", "哈哈哈哈哈哈哈～", message -> {
            // 给消息设置延迟毫秒值
            message.getMessageProperties().setExpiration(String.valueOf(ttl));
            return message;
        });
        return "success";
    }

}
