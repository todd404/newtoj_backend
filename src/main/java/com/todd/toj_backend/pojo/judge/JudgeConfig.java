package com.todd.toj_backend.pojo.judge;

import com.todd.toj_backend.pojo.problem.ProblemConfig;
import lombok.Data;

@Data
public class JudgeConfig {
    String language;
    String problemId;
    String uuid;
    String type;
    String forUUID;
    String code;
    ProblemConfig problemConfig;
}
