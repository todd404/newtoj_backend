package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.ChoiceProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChoiceProblemController {
    @Autowired
    ChoiceProblemService choiceProblemService;

    @PostMapping("/add-choice-problem")
    public ResponseResult addChoiceProblem(@RequestBody ChoiceProblemDao choiceProblemDao){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        choiceProblemDao.setUserId(loginUser.getUser().getUserId().toString());
        choiceProblemService.addChoiceProblem(choiceProblemDao);

        return new ResponseResult(200, "");
    }
}
