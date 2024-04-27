package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.List;

@Data
public class AttendExam {
    String type;
    String title;
    List<ExamWithScore> attendExamItemList;
}
