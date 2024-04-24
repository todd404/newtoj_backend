package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.pojo.problem.ProgramProblemItem;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        String userId = "";
        if(loginUser == null){
            userId = "0";
        }else{
            userId = loginUser.getUser().getUserId().toString();
        }
        List<ProblemsetItem> problemset = problemService.getProblemset(userId);
        return new ResponseResult(200, problemset);
    }

    @GetMapping("/problem")
    public ResponseResult getProblem(@RequestParam("problemId") String problemId){
        Problem problem = problemService.getProblem(problemId);
        return new ResponseResult<>(200, problem);
    }

    @GetMapping("/program-problem-list")
    public ResponseResult getProgramProblemList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        List<ProgramProblemItem> programProblemItems = problemService.getProgramProblemList(loginUser.getUser().getUserId().toString());
        return new ResponseResult<>(200, programProblemItems);
    }
}
