package com.todd.toj_backend.pojo.exam;

import lombok.Data;

@Data
public class ExamScore {
    Integer id;
    Integer examId;
    Integer userId;
    String username;
    String nickname;
    Double score;
    Integer timeUsed;
}
