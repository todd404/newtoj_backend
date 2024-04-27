package com.todd.toj_backend.pojo.exam;

import lombok.Data;

@Data
public class ExamResult {
    Integer id;
    Integer examId;
    Integer userId;
    Float score;
    Integer timeUsed;
}
