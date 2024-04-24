package com.todd.toj_backend.pojo.exam;

import lombok.Data;

@Data
public class ExamItem {
    Integer id;
    String type;
    String title;
    Integer score;
}
