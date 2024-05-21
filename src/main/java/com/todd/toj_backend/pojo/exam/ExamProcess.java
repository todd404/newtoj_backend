package com.todd.toj_backend.pojo.exam;

import com.todd.toj_backend.pojo.problem.ProblemAnswer;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExamProcess {
    String uuid;
    String userId;
    String examId;
    Date startTime;
    Integer timeLimit;
    List<Float> scoreList;
    List<ExamItem> examItemList;
    Map<Integer, List<String>> choiceProblemAnswerMap = new HashMap<>();
    Map<Integer, ProblemAnswer> programProblemAnswerMap = new HashMap<>();
}
