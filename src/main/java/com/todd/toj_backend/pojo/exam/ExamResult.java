package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExamResult {
    Integer id;
    Integer examId;
    Integer userId;
    Float score;
    Integer timeUsed;
    List<ChoiceProblemAnswer> answerCollect = new ArrayList<>();
}
