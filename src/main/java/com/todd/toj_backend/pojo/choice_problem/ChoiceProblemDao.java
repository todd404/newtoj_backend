package com.todd.toj_backend.pojo.choice_problem;

import lombok.Data;

import java.util.List;

@Data
public class ChoiceProblemDao {
    Integer id;
    String title;
    String userId;
    List<ChoiceProblem> choiceProblemList;
}
