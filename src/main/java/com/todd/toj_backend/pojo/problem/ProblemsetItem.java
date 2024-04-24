package com.todd.toj_backend.pojo.problem;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ProblemsetItem {
    String id;
    Boolean passed;
    String title;
    String passingRate = "0";
    List<String> tags;
}
