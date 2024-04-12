package com.todd.toj_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.mq.mq_sender.JudgeMQSender;
import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.judge.JudgeConfig;
import com.todd.toj_backend.pojo.judge.JudgeResponse;
import com.todd.toj_backend.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JudgeController {
    @Autowired
    JudgeMQSender judgeMQSender;

    @Autowired
    JudgeService judgeService;

    @PostMapping("/judge")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseResult judge(@RequestBody JudgeConfig judgeConfig){
        JudgeResponse judgeResponse = null;
        try {
            judgeResponse = judgeService.judge(judgeConfig);
        } catch (JsonProcessingException e) {
            return new ResponseResult(500, "内部错误");
        }
        return new ResponseResult(200, judgeResponse);
    }

    @PostMapping("/judge_status")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseResult status(@RequestBody JudgeResponse judgeResponse){
        var result = judgeService.getJudgeStatus(judgeResponse.getUuid());
        if(result == null){
            return new ResponseResult(500, "uuid错误");
        }else{
            return new ResponseResult(200, result);
        }
    }
}
