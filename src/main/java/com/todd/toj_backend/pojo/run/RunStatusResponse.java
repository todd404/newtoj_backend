package com.todd.toj_backend.pojo.run;

import lombok.Data;

@Data
public class RunStatusResponse {
    Integer statusCode;
    String msg;
    String runResult;
}
