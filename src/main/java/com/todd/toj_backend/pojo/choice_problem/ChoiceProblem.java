package com.todd.toj_backend.pojo.choice_problem;

import lombok.Data;

import java.util.List;

@Data
public class ChoiceProblem {
    Integer id;
    String problemContent;
    List<String> choiceList;
    String answer;
}
