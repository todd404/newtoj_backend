package com.todd.toj_backend.pojo.problem.add_problem;

import com.todd.toj_backend.pojo.problem.ProblemConfig;
import lombok.Data;

import java.util.List;

@Data
public class AddProblemRequest {
    String title;
    String content;
    String code;
    String language;
    ProblemConfig problemConfig;
    ProblemCaseConfig problemCaseConfig;
    List<String> problemTags;
}
