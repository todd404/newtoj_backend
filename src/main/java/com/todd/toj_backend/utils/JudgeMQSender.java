package com.todd.toj_backend.utils;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JudgeMQSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg){
        rabbitTemplate.convertAndSend("JudgeQueue", msg);
    }
}
