package com.todd.toj_backend.pojo.exam;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserExamChoiceAnswer {
    Integer userId;
    String username;
    String nickname;
    Float score;
    List<String> answerList = new ArrayList<>();
}
