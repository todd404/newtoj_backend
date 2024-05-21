package com.todd.toj_backend.pojo.problem;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemAnswer {
    Integer problemId;
    Boolean basicPassed = false;
    List<Boolean> specialCasePassList = new ArrayList<>();
    String code;
}
