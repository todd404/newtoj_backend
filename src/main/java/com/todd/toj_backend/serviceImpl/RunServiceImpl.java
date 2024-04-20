package com.todd.toj_backend.serviceImpl;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mq.mq_sender.RunMQSender;
import com.todd.toj_backend.pojo.run.RunConfig;
import com.todd.toj_backend.pojo.run.RunReport;
import com.todd.toj_backend.pojo.run.RunRequest;
import com.todd.toj_backend.pojo.run.RunStatusResponse;
import com.todd.toj_backend.service.RunService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RunServiceImpl implements RunService {
    @Autowired
    RunMQSender runMQSender;

    @Autowired
    RedisCache redisCache;

    @Override
    public String runForResult(RunRequest runRequest) throws IOException {
        String uuid = UUID.randomUUID().toString();

        String basePath = "D:/toj_files/run/" + uuid;
        String templateFilePath = basePath + "/template.cpp";
        String testFilePath = basePath + "/test.txt";

        String templateFileSrc = "D:/toj_files/cpp/template/" + runRequest.getProblemId() + "." + runRequest.getLanguage();
        Files.createDirectory(Path.of(basePath));
        FileUtil.copy(Paths.get(templateFileSrc), Paths.get(templateFilePath));
        FileUtil.writeString(runRequest.getRunCase(), testFilePath, StandardCharsets.UTF_8);


        RunConfig runConfig = new RunConfig();
        runConfig.setUuid(uuid);
        runConfig.setProblemId(runRequest.getProblemId());
        runConfig.setLanguage(runRequest.getLanguage());
        runConfig.setCode(runRequest.getCode());

        ObjectMapper objectMapper = new ObjectMapper();
        RunReport runReport = new RunReport();
        runReport.setStatusCode(100);
        redisCache.setCacheObject("run:" + uuid, objectMapper.writeValueAsString(runReport), 10, TimeUnit.MINUTES);
        runMQSender.send(runConfig);

        return uuid;
    }

    @Override
    public RunStatusResponse getRunStatus(String uuid) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = redisCache.getCacheObject("run:" + uuid);
        if(msg == null || msg == "")
            return null;

        String answerFilePath = "D:/toj_files/run/" + uuid + "/answer.txt";
        RunStatusResponse runStatusResponse = new RunStatusResponse();
        RunReport runReport = objectMapper.readValue(msg, RunReport.class);
        runStatusResponse.setStatusCode(runReport.getStatusCode());
        runStatusResponse.setMsg(runReport.getMsg());
        if(runReport.getStatusCode() == 200){
            String runResult = FileUtil.readString(answerFilePath, StandardCharsets.UTF_8);
            runStatusResponse.setRunResult(runResult);
        }else{
            runStatusResponse.setRunResult("");
        }

        return runStatusResponse;
    }
}
