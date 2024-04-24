package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.judge.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JudgeService {
    public JudgeResponse judge(JudgeConfig judgeConfig) throws JsonProcessingException;
    public JudgeReport queryJudgeReport(String uuid) throws JsonProcessingException;
    public JudgeStatusResponse getJudgeStatus(String uuid);
    public void solveJudgeFinish(String uuid) throws JsonProcessingException;
    public List<JudgeHistory> getJudgeHistory(String problemId, String userId);
}
