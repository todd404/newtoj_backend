package com.todd.toj_backend.pojo.judge;

import lombok.Data;

@Data
public class JudgeHistory {
    Integer id;
    String problemId;
    String userId;
    Boolean isPass;
    String status;
    String timeUsed;
    String memoryUsed;
    String finishAt;
}
