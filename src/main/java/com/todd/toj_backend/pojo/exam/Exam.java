package com.todd.toj_backend.pojo.exam;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ExamItem> examItemList;
}
