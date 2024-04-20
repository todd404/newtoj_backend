package com.todd.toj_backend.pojo.run;

import lombok.Data;

@Data
public class RunReport {
    Integer statusCode;
    String msg;
    RunConfig runConfig;
}
