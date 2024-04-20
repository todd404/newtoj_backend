package com.todd.toj_backend.pojo.run;

import lombok.Data;

@Data
public class RunRequest {
    String problemId;
    String language;
    String code;
    String runCase;
}
