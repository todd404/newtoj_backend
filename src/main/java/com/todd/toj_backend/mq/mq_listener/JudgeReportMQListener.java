package com.todd.toj_backend.mq.mq_listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.service.JudgeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JudgeReportMQListener {
    @Autowired
    JudgeService judgeService;

    @RabbitListener(queues = "judgeReportQueue")
    public void listenJudgeReportQueue(String uuid) throws JsonProcessingException {
        judgeService.solveJudgeFinish(uuid);
    }
}
