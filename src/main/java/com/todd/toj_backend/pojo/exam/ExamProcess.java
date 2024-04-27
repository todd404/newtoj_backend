package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExamProcess {
    String uuid;
    String userId;
    String examId;
    Date startTime;
    Integer timeLimit;
    List<Float> scoreList;
    List<ExamItem> examItemList;
}
