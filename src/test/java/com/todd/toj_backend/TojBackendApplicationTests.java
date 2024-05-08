package com.todd.toj_backend;

import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mq.mq_sender.RunMQSender;
import com.todd.toj_backend.service.CommentService;
import com.todd.toj_backend.mq.mq_sender.JudgeMQSender;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TojBackendApplicationTests {
    @Autowired
    CommentService commentService;

    @Autowired
    ProblemMapper problemMapper;

    @Autowired
    JudgeMQSender judgeMqSender;

    @Autowired
    RunMQSender runMQSender;

    @Autowired
    RedisCache redisCache;

//    @Test
//    void insertProblemTest() {
//        Problem problem = new Problem();
//        problem.setTitle("test");
//        problem.setContent("123");
//
//        ProblemConfig problemConfig = new ProblemConfig();
//        ScoreConfig scoreConfig = new ScoreConfig();
//        scoreConfig.setBasicCasesScore(50);
//        scoreConfig.setBasicCasesCount(3);
//        scoreConfig.setSpecialCasesScoreList(Arrays.asList(15, 15, 20));
//        problemConfig.setScoreConfig(scoreConfig);
//        problemConfig.setArgumentTypes(Arrays.asList("int[]", "int"));
//        problemConfig.setArgumentNames(Arrays.asList("nums", "num"));
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

//    @Test
//    void mqTest(){
//        ProblemConfig problemConfig = new ProblemConfig();
//        problemConfig.setArguments(Arrays.asList("int[]", "int"));
//        problemConfig.setMemoryLimit(10000);
//        problemConfig.setFunctionName("allAdd");
//        problemConfig.setReturnType("int[]");
//        problemConfig.setTimeLimit(200000);
//
//        JudgeConfig judgeConfig = new JudgeConfig();
//        judgeConfig.setUuid("1234");
//        judgeConfig.setLanguage("cpp");
//        judgeConfig.setCode("""
//                class Solution {
//                public:
//                	vector<int> allAdd(vector<int> nums, int n) {
//                		for (int& num : nums) {
//                			num += n;
//                		}
//
//                		return nums;
//                	}
//                };
//                """);
//        judgeConfig.setProblemId("1");
//        judgeConfig.setProblemConfig(problemConfig);
//
//        judgeMqSender.send(judgeConfig);
//    }

//    @Test
//    void redisTest(){
//        String msg = redisCache.getCacheObject("judge:a4c4fca2-cfdf-4624-9ac9-388a7910d922");
//        return;
//    }

//    @Test
//    void runCodeForResultTest(){
//        RunConfig runConfig = new RunConfig();
//        runConfig.setUuid("123");
//        runConfig.setLanguage("cpp");
//        runConfig.setCode("""
//                class Solution {
//                public:
//                	vector<int> allAdd(vector<int> nums, int n) {
//                		for (int& num : nums) {
//                			num += n;
//                		}
//
//                		return nums;
//                	}
//                };
//                """);
//        runConfig.setProblemId("1");
//
//        runMQSender.send(runConfig);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        RunReport runReport = new RunReport();
//        runReport.setStatusCode(100);
//        runReport.setRunConfig(runConfig);
//        try {
//            String msg = objectMapper.writeValueAsString(runReport);
//            redisCache.setCacheObject("run:"+runConfig.getUuid(), msg);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

}
