package com.todd.toj_backend.pojo.problem;

import lombok.Data;

@Data
public class Problem {
    String id;
    String title;
    String content;
    ProblemConfig problemConfig;
}
