package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblem;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChoiceProblemService {
    Integer addChoiceProblem(ChoiceProblemDao choiceProblemDao);
    List<ChoiceProblemDao> getOwnChoiceProblemList(String userId);
    List<ChoiceProblem> getChoiceProblem(String problemId);

    void JudgeChoiceProblem(List<String> answerList, String examUUID, String choiceProblemId) throws JsonProcessingException;
}
