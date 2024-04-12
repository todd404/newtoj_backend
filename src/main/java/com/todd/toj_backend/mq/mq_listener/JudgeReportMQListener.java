package com.todd.toj_backend.mq.mq_listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class JudgeReportMQListener {
    @RabbitListener(queues = "judgeReportQueue")
    public void listenJudgeReportQueue(String msg){

    }
}
