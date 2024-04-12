package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.judge.JudgeConfig;
import com.todd.toj_backend.pojo.judge.JudgeReport;
import com.todd.toj_backend.pojo.judge.JudgeResponse;
import com.todd.toj_backend.pojo.judge.JudgeStatusResponse;
import org.springframework.stereotype.Service;

@Service
public interface JudgeService {
    public JudgeResponse judge(JudgeConfig judgeConfig) throws JsonProcessingException;
    public JudgeReport queryJudgeReport(String uuid) throws JsonProcessingException;
    public JudgeStatusResponse getJudgeStatus(String uuid);
}
