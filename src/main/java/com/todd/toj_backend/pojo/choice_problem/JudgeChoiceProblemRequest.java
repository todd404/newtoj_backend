package com.todd.toj_backend.pojo.choice_problem;

import lombok.Data;

import java.util.List;

@Data
public class JudgeChoiceProblemRequest {
    String examUUID;
    String choiceProblemId;
    List<String> answerList;
}
