package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Exam {
    Integer id;
    Integer userId;
    String title;
    Date startTime;
    Date endTime;
    Integer timeLimit;
    List<ExamItem> examItemList;
}
