package com.todd.toj_backend.utils;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.bean.file_builder.CppFileBuilder;
import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mq.mq_sender.RunMQSender;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.add_problem.AddProblemRequest;
import com.todd.toj_backend.pojo.run.RunConfig;
import com.todd.toj_backend.pojo.run.RunReport;
import com.todd.toj_backend.utils.add_problem.RandomCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Paths;

@Component
public class AddProblemAsync {
    @Autowired
    RunMQSender runMQSender;

    @Autowired
    RedisCache redisCache;

    @Autowired
    CppFileBuilder cppFileBuilder;

    @Autowired
    ProblemMapper problemMapper;
    @Async
    @Transactional
    public void addProblemProcess(AddProblemRequest addProblemRequest, String uuid) throws IOException, InterruptedException {
        String basePath = "D:/toj_files/run/" + uuid;
        String templateFilePath = basePath + "/template.cpp";
        String testFilePath = basePath + "/test.txt";
        String answerFilePath = basePath + "/answer.txt";

        cppFileBuilder.buildCppFile(addProblemRequest.getProblemConfig(), templateFilePath);

        if(addProblemRequest.getProblemCaseConfig().getCaseUploadedFile() != ""){
            FileUtil.copy(Paths.get("D:/toj_files/temp/" + addProblemRequest.getProblemCaseConfig().getCaseUploadedFile()),
                    Paths.get(testFilePath));
        }else{
            StringBuilder testCaseContent = new StringBuilder();
            for(int i = 0; i < addProblemRequest.getProblemCaseConfig().getAutoCaseCount(); i++){
                var argTypeList = addProblemRequest.getProblemConfig().getArgumentTypeList();
                for(int j = 0; j < argTypeList.size(); j++){
                    String result = RandomCase.getRandomCase(argTypeList.get(j),
                            addProblemRequest.getProblemCaseConfig().getPerArgAutoCaseConfigList().get(j),
                            addProblemRequest.getProblemCaseConfig().getAutoCaseCount(), i
                    );
                    testCaseContent.append(result + "\n");
                }
            }

            testCaseContent.append(addProblemRequest.getProblemCaseConfig().getSpecialCase());
            FileUtil.writeUtf8String(testCaseContent.toString(), testFilePath);
        }

        RunConfig runConfig = new RunConfig();
        runConfig.setUuid(uuid);
        runConfig.setProblemId(uuid);
        runConfig.setCode(addProblemRequest.getCode());
        runConfig.setLanguage(addProblemRequest.getLanguage());

        runMQSender.send(runConfig);

        while(true){
            ObjectMapper objectMapper = new ObjectMapper();
            String msg = redisCache.getCacheObject("run:" + uuid);
            RunReport runReport = objectMapper.readValue(msg, RunReport.class);
            if(runReport.getStatusCode() < 200){
                Thread.sleep(1000);
                continue;
            }

            if(runReport.getStatusCode() == 200){
                Problem problem = new Problem();
                problem.setTitle(addProblemRequest.getTitle());
                problem.setContent(addProblemRequest.getContent());
                problem.setProblemConfig(addProblemRequest.getProblemConfig());

                problemMapper.insertProblem(problem);
                problemMapper.insertProblemTags(problem.getId(), addProblemRequest.getProblemTags());

                FileUtil.move(Paths.get(templateFilePath), Paths.get("D:/toj_files/cpp/template/" + problem.getId() + ".cpp"), true);
                FileUtil.move(Paths.get(testFilePath), Paths.get("D:/toj_files/test/" + problem.getId() + ".txt"), true);
                FileUtil.move(Paths.get(answerFilePath), Paths.get("D:/toj_files/answer/" + problem.getId() + ".txt"), true);
            }

            break;
        }
    }
}
