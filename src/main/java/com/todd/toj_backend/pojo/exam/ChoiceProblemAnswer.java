package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChoiceProblemAnswer {
    Integer choiceProblemId;
    List<String> answerList = new ArrayList<>();
}
