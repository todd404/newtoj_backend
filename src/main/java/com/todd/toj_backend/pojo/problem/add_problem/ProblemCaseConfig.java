package com.todd.toj_backend.pojo.problem.add_problem;

import lombok.Data;

import java.util.List;

@Data
public class ProblemCaseConfig {
    String caseUploadedFile = "";
    Integer autoCaseCount;
    List<PerArgAutoCaseConfig> perArgAutoCaseConfigList;
    String specialCase;
}
