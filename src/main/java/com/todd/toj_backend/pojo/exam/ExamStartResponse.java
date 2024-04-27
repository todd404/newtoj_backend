package com.todd.toj_backend.pojo.exam;

import lombok.Data;

@Data
public class ExamStartResponse {
    Boolean allowExam = false;
    String uuid;
    Integer timeLimit;
}
