package com.todd.toj_backend.pojo.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemConfig implements Serializable {
    String functionName;
    String returnType;
    List<String> argumentTypes = new ArrayList<>();
    List<String> argumentNames = new ArrayList<>();
    Integer timeLimit;
    Integer memoryLimit;
    ScoreConfig scoreConfig;
}
