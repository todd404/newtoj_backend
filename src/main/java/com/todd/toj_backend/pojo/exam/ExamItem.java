package com.todd.toj_backend.pojo.exam;

import lombok.Data;

@Data
public class ExamItem {
    Integer problemId;
    String type;
    String title;
    Integer score;
}
