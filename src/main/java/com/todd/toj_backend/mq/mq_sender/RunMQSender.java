package com.todd.toj_backend.mq.mq_sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.pojo.run.RunConfig;
import com.todd.toj_backend.utils.RedisCache;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunMQSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

    public void send(RunConfig runConfig){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String msg = objectMapper.writeValueAsString(runConfig);
            rabbitTemplate.convertAndSend(runConfig.getLanguage() + "RunQueue", msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
