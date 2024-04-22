package com.todd.toj_backend.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mapper.JudgeHistoryMapper;
import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mq.mq_sender.JudgeMQSender;
import com.todd.toj_backend.pojo.judge.*;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.service.JudgeService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    JudgeMQSender judgeMQSender;

    @Autowired
    JudgeHistoryMapper judgeHistoryMapper;

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
        judgeReport.setJudgeConfig(judgeConfig);
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
        //TODO:添加功能：行号映射
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
        if(judgeReport.getStatusCode() < 200 || judgeReport.getStatusCode() >= 300){
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

    @Override
    public void solveJudgeFinish(String uuid) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = redisCache.getCacheObject("judge:" + uuid);
        JudgeReport judgeReport = objectMapper.readValue(msg, JudgeReport.class);

        String type = judgeReport.getJudgeConfig().getType();
        if(Objects.equals(type, "normal")){
            solveNormalJudge(judgeReport);
        }
    }

    private void solveNormalJudge(JudgeReport judgeReport){
        JudgeHistory judgeHistory = new JudgeHistory();
        judgeHistory.setProblemId(judgeReport.getJudgeConfig().getProblemId());
        judgeHistory.setUserId(judgeReport.getJudgeConfig().getForUUID());
        if(judgeReport.getStatusCode() != 200){
            judgeHistory.setIsPass(false);
        }else{
            judgeHistory.setIsPass(true);
        }

        if(judgeReport.getStatusCode() == 400){
            judgeHistory.setStatus("编译错误");
        }else if(judgeReport.getStatusCode() == 200){
            judgeHistory.setStatus("通过");
        } else {
            if(judgeReport.getMsg().contains("超时")){
                judgeHistory.setStatus("超过时间限制");
            }else if(judgeReport.getMsg().contains("内存")){
                judgeHistory.setStatus("超过内存限制");
            }else{
                judgeHistory.setStatus("运行错误");
            }
        }

        if(judgeReport.getMemoryUsed() == null || judgeReport.getTimeUsed() == null){
            judgeHistory.setMemoryUsed("N/A");
            judgeHistory.setTimeUsed("N/A");
        }else{
            judgeHistory.setMemoryUsed(judgeReport.getMemoryUsed());
            judgeHistory.setTimeUsed(judgeReport.getTimeUsed());
        }

        judgeHistoryMapper.insertJudgeHistory(judgeHistory);
    }
}
