package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.ChoiceProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChoiceProblemController {
    @Autowired
    ChoiceProblemService choiceProblemService;

    @PostMapping("/add-choice-problem")
    public ResponseResult addChoiceProblem(@RequestBody ChoiceProblemDao choiceProblemDao){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        choiceProblemDao.setUserId(loginUser.getUser().getUserId().toString());
        choiceProblemService.addChoiceProblem(choiceProblemDao);

        return new ResponseResult(200, "");
    }

    @GetMapping("/choice-problem")
    public ResponseResult getChoiceProblemList(@RequestParam("problemId") String problemId){
        var result = choiceProblemService.getChoiceProblem(problemId);
        return new ResponseResult(200, result);
    }

    @GetMapping("/own-choice-problem-list")
    public ResponseResult getOwnChoiceProblemList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        List<ChoiceProblemDao> choiceProblemList = choiceProblemService.getOwnChoiceProblemList(loginUser.getUser().getUserId().toString());

        return new ResponseResult<>(200, choiceProblemList);
    }
}
