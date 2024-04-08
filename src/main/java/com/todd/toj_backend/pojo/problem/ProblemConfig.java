package com.todd.toj_backend.pojo.problem;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemConfig implements Serializable {
    String functionName;
    String returnType;
    List<String> arguments = new ArrayList<>();
    Integer timeLimit;
    Integer memoryLimit;
}
