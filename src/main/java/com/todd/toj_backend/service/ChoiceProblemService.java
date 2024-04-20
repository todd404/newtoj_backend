package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import org.springframework.stereotype.Service;

@Service
public interface ChoiceProblemService {
    Integer addChoiceProblem(ChoiceProblemDao choiceProblemDao);
}
