package com.todd.toj_backend.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.bean.file_builder.CppFileBuilder;
import com.todd.toj_backend.mapper.JudgeHistoryMapper;
import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mq.mq_sender.RunMQSender;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.pojo.problem.ProgramProblemItem;
import com.todd.toj_backend.pojo.problem.add_problem.AddProblemRequest;
import com.todd.toj_backend.pojo.run.RunReport;
import com.todd.toj_backend.service.ProblemService;
import com.todd.toj_backend.utils.AddProblemAsync;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    CppFileBuilder cppFileBuilder;

    @Autowired
    RunMQSender runMQSender;

    @Autowired
    AddProblemAsync addProblemAsync;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    JudgeHistoryMapper judgeHistoryMapper;

    private RunReport pullRunReport(String uuid) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = redisCache.getCacheObject("run:" + uuid);
        return objectMapper.readValue(result, RunReport.class);
    }

    private void putRunReport(String uuid, RunReport runReport) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        redisCache.setCacheObject("run:" + uuid, objectMapper.writeValueAsString(runReport));
    }

    @Override
    public List<ProblemsetItem> getProblemset(String userId) {
        List<ProblemsetItem> problemset = problemMapper.queryProblemset();
        for(var p : problemset){
            Double passRate = judgeHistoryMapper.queryProblemPassRate(p.getId());
            Boolean isPass = judgeHistoryMapper.queryIsProblemPass(p.getId(), userId);

            if(passRate == null){
                passRate = 0.0;
            }
            if(isPass == null){
                isPass = false;
            }
            p.setPassed(isPass);
            p.setPassingRate(String.valueOf(passRate));
        }

        return problemset;
    }

    @Override
    public Problem getProblem(String problemId) {
        return problemMapper.queryProblem(problemId);
    }

    @Override
    public List<ProgramProblemItem> getProgramProblemList(String userId) {
        return problemMapper.queryProgramProblemList(userId);
    }

    @Override
    public String addProblem(AddProblemRequest addProblemRequest) throws IOException, InterruptedException {
        String uuid = UUID.randomUUID().toString();
        RunReport runReport = new RunReport();
        runReport.setStatusCode(100);
        runReport.setMsg("运行中");
        putRunReport(uuid, runReport);

        addProblemAsync.addProblemProcess(addProblemRequest, uuid);

        return uuid;
    }


}
