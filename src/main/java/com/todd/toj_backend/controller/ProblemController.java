package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProblemController {
    @Autowired
    ProblemService problemService;
    @GetMapping("/problemset")
    public ResponseResult getProblemset(){
        List<ProblemsetItem> problemset = problemService.getProblemset();
        return new ResponseResult(200, problemset);
    }

    @GetMapping("/problem")
    ResponseResult getProblem(@RequestParam("problemId") String problemId){
        Problem problem = problemService.getProblem(problemId);
        return new ResponseResult<>(200, problem);
    }
}
