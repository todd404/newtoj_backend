package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.job.AddJobRequest;
import com.todd.toj_backend.pojo.job.AttendJob;
import com.todd.toj_backend.pojo.job.Job;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobService jobService;

    @GetMapping("/all-job")
    public ResponseResult getAllJob(){
        return new ResponseResult<>(200, jobService.getUnexpiredJobList());
    }

    @GetMapping("/job-enroll")
    public ResponseResult getJobEnroll (){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        return new ResponseResult(200, jobService.getJobEnrollList(loginUser.getUser().getUserId()));
    }

    @GetMapping("/my-job-list")
    public ResponseResult getMyJobList (){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        return new ResponseResult(200, jobService.getOwnJobList(loginUser.getUser().getUserId()));
    }

    @PostMapping("/add-job")
    public ResponseResult addJob(@RequestBody AddJobRequest job){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        job.setUserId(loginUser.getUser().getUserId());
        var result = jobService.addJob(job);
        return new ResponseResult(200, "");
    }

    @PostMapping("/add-job-enroll")
    public ResponseResult addJobEnroll(@RequestBody AttendJob attendJob){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        attendJob.setUserId(loginUser.getUser().getUserId());
        jobService.addJobEnroll(attendJob.getUserId(), attendJob.getJobId());
        return new ResponseResult(200, "");
    }

    @PostMapping("remove-job-enroll")
    public ResponseResult removeJobEnroll(@RequestBody AttendJob attendJob){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        attendJob.setUserId(loginUser.getUser().getUserId());
        jobService.removeJobEnroll(attendJob.getUserId(), attendJob.getJobId());
        return new ResponseResult(200, "");
    }
}
