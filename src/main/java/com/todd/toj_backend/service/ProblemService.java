package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.pojo.problem.add_problem.AddProblemRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface ProblemService {
     List<ProblemsetItem> getProblemset();
     Problem getProblem(String problemId);
     String addProblem(AddProblemRequest addProblemRequest) throws IOException, InterruptedException;
}