package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.ChoiceProblemMapper;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.service.ChoiceProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceProblemImpl implements ChoiceProblemService {
    @Autowired
    ChoiceProblemMapper choiceProblemMapper;

    @Override
    public Integer addChoiceProblem(ChoiceProblemDao choiceProblemDao) {
        return choiceProblemMapper.insetChoiceProblem(choiceProblemDao);
    }
}
