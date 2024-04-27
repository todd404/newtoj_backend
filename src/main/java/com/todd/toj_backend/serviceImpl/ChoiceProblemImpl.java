package com.todd.toj_backend.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mapper.ChoiceProblemMapper;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblem;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.pojo.exam.ExamItem;
import com.todd.toj_backend.pojo.exam.ExamProcess;
import com.todd.toj_backend.service.ChoiceProblemService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChoiceProblemImpl implements ChoiceProblemService {
    @Autowired
    ChoiceProblemMapper choiceProblemMapper;

    @Autowired
    RedisCache redisCache;

    @Override
    public Integer addChoiceProblem(ChoiceProblemDao choiceProblemDao) {
        return choiceProblemMapper.insetChoiceProblem(choiceProblemDao);
    }

    @Override
    public List<ChoiceProblemDao> getOwnChoiceProblemList(String userId) {
        return choiceProblemMapper.queryOwnChoiceProblem(userId);
    }

    @Override
    public List<ChoiceProblem> getChoiceProblem(String problemId) {
        ChoiceProblemDao choiceProblemDao = choiceProblemMapper.queryChoiceProblem(problemId);
        List<ChoiceProblem> choiceProblemList = choiceProblemDao.getChoiceProblemList();
        String s = JSONUtil.toJsonStr(choiceProblemList);
        List<ChoiceProblem> result = JSONUtil.toList(s, ChoiceProblem.class);
        for(var problem : result){
            problem.setAnswer("");
        }

        return result;
    }

    @Override
    public void JudgeChoiceProblem(List<String> answerList, String examUUID, String choiceProblemId) throws JsonProcessingException {
        String msg = redisCache.getCacheObject("exam:" + examUUID);
        if(msg == null) return;
        ObjectMapper objectMapper = new ObjectMapper();
        ExamProcess examProcess = objectMapper.readValue(msg, ExamProcess.class);
        if(new Date().getTime() > examProcess.getStartTime().getTime() + (examProcess.getTimeLimit() * 1000 * 60)){
            return;
        }

        ChoiceProblemDao choiceProblemDao = choiceProblemMapper.queryChoiceProblem(choiceProblemId);
        String choiceProblemListJsonStr = JSONUtil.toJsonStr(choiceProblemDao.getChoiceProblemList());
        List<ChoiceProblem> choiceProblemList = JSONUtil.toList(choiceProblemListJsonStr, ChoiceProblem.class);

        int correctCount = 0;
        for(int i = 0; i < choiceProblemList.size(); i++){
            if(answerList.get(i).equals(choiceProblemList.get(i).getAnswer())){
                correctCount++;
            }
        }

        Double correctRate = (double) correctCount / (double) choiceProblemList.size();

        List<ExamItem> examItemList = examProcess.getExamItemList();
        List<Float> scoreList = examProcess.getScoreList();
        int scoreIndex = -1;
        for(int i = 0; i < examItemList.size(); i++){
            if(!examItemList.get(i).getType().equals("choice")){
                continue;
            }

            if(examItemList.get(i).getProblemId().toString().equals(choiceProblemId)){
                scoreIndex = i;
                break;
            }
        }

        Float score = (float) (examProcess.getExamItemList().get(scoreIndex).getScore() * correctRate);
        scoreList.set(scoreIndex, score);

        examProcess.setScoreList(scoreList);
        redisCache.setCacheObject("exam:"+examUUID, objectMapper.writeValueAsString(examProcess));
    }
}
