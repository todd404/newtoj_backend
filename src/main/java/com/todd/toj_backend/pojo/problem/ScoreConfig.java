package com.todd.toj_backend.pojo.problem;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScoreConfig {
    Integer basicCasesCount;
    Integer basicCasesScore;
    List<Integer> specialCasesScoreList = new ArrayList<>();
}
