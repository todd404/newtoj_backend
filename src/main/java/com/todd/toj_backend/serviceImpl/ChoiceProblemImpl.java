package com.todd.toj_backend.serviceImpl;

import cn.hutool.json.JSONUtil;
import com.todd.toj_backend.mapper.ChoiceProblemMapper;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblem;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.service.ChoiceProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChoiceProblemImpl implements ChoiceProblemService {
    @Autowired
    ChoiceProblemMapper choiceProblemMapper;

    @Override
    public Integer addChoiceProblem(ChoiceProblemDao choiceProblemDao) {
        return choiceProblemMapper.insetChoiceProblem(choiceProblemDao);
    }

    @Override
    public List<ChoiceProblemDao> getOwnChoiceProblemList(String userId) {
        return choiceProblemMapper.queryOwnChoiceProblem(userId);
    }

    @Override
    public List<ChoiceProblem> getChoiceProblem(String problemId) {
        ChoiceProblemDao choiceProblemDao = choiceProblemMapper.queryChoiceProblem(problemId);
        List<ChoiceProblem> choiceProblemList = choiceProblemDao.getChoiceProblemList();
        String s = JSONUtil.toJsonStr(choiceProblemList);
        List<ChoiceProblem> result = JSONUtil.toList(s, ChoiceProblem.class);
        for(var problem : result){
            problem.setAnswer("");
        }

        return result;
    }
}
