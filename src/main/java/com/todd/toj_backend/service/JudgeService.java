package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.judge.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JudgeService {
    JudgeResponse judge(JudgeConfig judgeConfig) throws JsonProcessingException;
    JudgeReport queryJudgeReport(String uuid) throws JsonProcessingException;
    JudgeStatusResponse getJudgeStatus(String uuid);
    void solveJudgeFinish(String uuid) throws JsonProcessingException;
    List<JudgeHistory> getJudgeHistory(String problemId, String userId);
}
