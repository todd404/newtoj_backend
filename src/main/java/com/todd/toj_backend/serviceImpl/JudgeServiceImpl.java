package com.todd.toj_backend.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mq.mq_sender.JudgeMQSender;
import com.todd.toj_backend.pojo.judge.JudgeConfig;
import com.todd.toj_backend.pojo.judge.JudgeReport;
import com.todd.toj_backend.pojo.judge.JudgeResponse;
import com.todd.toj_backend.pojo.judge.JudgeStatusResponse;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.service.JudgeService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    JudgeMQSender judgeMQSender;

    @Override
    public JudgeResponse judge(JudgeConfig judgeConfig) throws JsonProcessingException {
        String uuid = UUID.randomUUID().toString();
        judgeConfig.setUuid(uuid);
        Problem problem = problemMapper.queryProblem(judgeConfig.getProblemId());
        if(problem == null)
            return null;
        judgeConfig.setProblemConfig(problem.getProblemConfig());

        ObjectMapper objectMapper = new ObjectMapper();
        JudgeReport judgeReport = new JudgeReport();
        judgeReport.setStatusCode(100);
        redisCache.setCacheObject("judge:" + judgeConfig.getUuid(), objectMapper.writeValueAsString(judgeReport));

        judgeMQSender.send(judgeConfig);

        return new JudgeResponse(uuid);
    }

    @Override
    public JudgeReport queryJudgeReport(String uuid) throws JsonProcessingException {
        String msg = redisCache.getCacheObject("judge:" + uuid);
        if(msg == null){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(msg, JudgeReport.class);
    }

    @Override
    public JudgeStatusResponse getJudgeStatus(String uuid) {
        JudgeReport judgeReport = null;
        try {
            judgeReport = queryJudgeReport(uuid);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        if(judgeReport == null){
            return null;
        }

        JudgeStatusResponse result = new JudgeStatusResponse();
        if(judgeReport.getStatusCode() < 200 || judgeReport.getStatusCode() > 500){
            return new JudgeStatusResponse(judgeReport.getStatusCode(), judgeReport.getMsg());
        }

        result.setCode(judgeReport.getStatusCode());
        JudgeConfig judgeConfig = judgeReport.getJudgeConfig();
        if(!judgeReport.getBasicCasesPassed()){
            result.setCode(201);
            //TODO: 返回具体用例
            result.setMsg("第%d个用例未通过".formatted(judgeReport.getBasicCasesCorrectLine()));
            return result;
        }else{
            Integer count = 0;
            for(var caze : judgeReport.getSpecialCasesPassedList()){
                if(caze){
                    count++;
                }else{
                    result.setCode(201);
                    //TODO: 返回具体用例
                    result.setMsg("第%d个用例未通过".formatted(judgeReport.getBasicCasesCorrectLine() + count + 1));
                    return result;
                }
            }
        }

        return result;
    }
}
