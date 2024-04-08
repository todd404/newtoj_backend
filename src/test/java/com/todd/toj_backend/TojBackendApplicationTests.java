package com.todd.toj_backend;

import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.pojo.problem.ProblemConfig;
import com.todd.toj_backend.service.CommentService;
import com.todd.toj_backend.utils.JudgeMQSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class TojBackendApplicationTests {
    @Autowired
    CommentService commentService;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    JudgeMQSender judgeMqSender;

//    @Test
//    void insertProblemTest() {
//        Problem problem = new Problem();
//        problem.setTitle("test");
//        problem.setContent("123");
//        ProblemConfig problemConfig = new ProblemConfig();
//        problemConfig.setArguments(Arrays.asList("int[]", "int"));
//        problemConfig.setMemoryLimit(10000);
//        problemConfig.setFunctionName("allAdd");
//        problemConfig.setReturnType("int[]");
//        problemConfig.setTimeLimit(200000);
//        problem.setProblemConfig(problemConfig);
//
//        problemMapper.insertProblem(problem);
//        return;
//    }

//    @Test
//    void queryProblemTest(){
//        Problem problem = problemMapper.queryProblem("2");
//        return;
//    }

    @Test
    void mqTest(){
        ProblemConfig problemConfig = new ProblemConfig();
        problemConfig.setArguments(Arrays.asList("int[]", "int"));
        problemConfig.setMemoryLimit(10000);
        problemConfig.setFunctionName("allAdd");
        problemConfig.setReturnType("int[]");
        problemConfig.setTimeLimit(200000);
        judgeMqSender.send(problemConfig);
    }
}
