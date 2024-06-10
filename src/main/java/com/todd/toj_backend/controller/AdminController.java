package com.todd.toj_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.Uuid;
import com.todd.toj_backend.pojo.problem.add_problem.AddProblemRequest;
import com.todd.toj_backend.pojo.run.RunReport;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.service.ProblemService;
import com.todd.toj_backend.service.UserService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    ProblemService problemService;

    @Autowired
    UserService userService;

    @Autowired
    RedisCache redisCache;

    @GetMapping("/all-user")
    public ResponseResult getAllUser(){
        return new ResponseResult(200, userService.getAllUserList());
    }

    @PostMapping("/add-problem")
    public ResponseResult addProblem(@RequestBody AddProblemRequest addProblemRequest) throws IOException, InterruptedException {
        String uuid = problemService.addProblem(addProblemRequest);

        return new ResponseResult<>(200, new Uuid(uuid));
    }

    @GetMapping("/query-add-problem-status")
    public ResponseResult queryAddProblemStatus(@RequestParam("uuid") String uuid) throws JsonProcessingException {
        String msg = redisCache.getCacheObject("run:" + uuid);
        if(msg == null || msg == ""){
            return new ResponseResult<>(500, "查询状态失败");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        RunReport runReport = objectMapper.readValue(msg, RunReport.class);

        return new ResponseResult(200, runReport);
    }


    @PostMapping("/change-user-role")
    public ResponseResult changeUserRole(@RequestBody User user){
        int result = userService.changeUserRole(user);
        if(result == 0){
            return new ResponseResult<>(500, "不存在角色");
        }

        return new ResponseResult<>(200, "");
    }
}
