package com.todd.toj_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.course.CourseExam;
import com.todd.toj_backend.pojo.exam.*;
import com.todd.toj_backend.pojo.job.JobExam;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamController {
    @Autowired
    ExamService examService;

    @GetMapping("/exam-item-list")
    public ResponseResult getExamItemList(@RequestParam("examId") String examId){
        List<ExamItem> examItemList = examService.getExamItemList(examId);
        if(examItemList == null){
            return new ResponseResult(500, "");
        }

        return new ResponseResult<>(200, examItemList);
    }

    @GetMapping("/attend-exam-list")
    public ResponseResult getAttendExamList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        List<AttendExam> attendExamList = examService.getAttendExamList(loginUser.getUser().getUserId().toString());
        return new ResponseResult<>(200, attendExamList);
    }

    @GetMapping("/my-exam-list")
    public ResponseResult myExamList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        List<Exam> myExamList = examService.getOwnExamList(loginUser.getUser().getUserId().toString());
        return new ResponseResult(200, myExamList);
    }

    @GetMapping("/score-list")
    public ResponseResult examScoreList(@RequestParam String examId){
        List<ExamScore> examScoreList = examService.getExamScoreList(examId);

        return new ResponseResult(200, examScoreList);
    }

    @GetMapping("/course-exam-list")
    public ResponseResult courseExamList(@RequestParam("courseId") String courseId){
        return new ResponseResult(200, examService.getCourseExamList(courseId));
    }

    @GetMapping("/job-exam-list")
    public ResponseResult jobExamList(@RequestParam("jobId") String jobId){
        return new ResponseResult(200, examService.getJobExamList(jobId));
    }

    @PostMapping("/add-exam")
    public ResponseResult addExam(@RequestBody Exam exam){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        exam.setUserId(loginUser.getUser().getUserId());
        var result = examService.addExam(exam);
        if(result == 0)
            return new ResponseResult<>(500, "内部错误");

        return new ResponseResult<>(200, "");
    }

    @PostMapping("/add-course-exam")
    public ResponseResult addCourseExam(@RequestBody CourseExam courseExam){
        int result = examService.addCourseExam(courseExam);
        if(result == 0){
            return new ResponseResult<>(500, "");
        }
        return new ResponseResult<>(200, "");
    }

    @PostMapping("/add-job-exam")
    public ResponseResult addJobExam(@RequestBody JobExam jobExam){
        int result = examService.addJobExam(jobExam);
        if(result == 0){
            return new ResponseResult<>(500, "");
        }
        return new ResponseResult<>(200, "");
    }

    @PostMapping("/delete-course-exam")
    public ResponseResult removeCourseExam(@RequestBody CourseExam courseExam){
        int result = examService.deleteCourseExam(courseExam);

        if(result == 0){
            return new ResponseResult<>(500, "");
        }
        return new ResponseResult<>(200, "");
    }

    @PostMapping("/delete-job-exam")
    public ResponseResult removeJobExam(@RequestBody JobExam jobExam){
        int result = examService.deleteJobExam(jobExam);

        if(result == 0){
            return new ResponseResult<>(500, "");
        }
        return new ResponseResult<>(200, "");
    }

    @GetMapping("/start-exam")
    public ResponseResult startExam(@RequestParam("examId") String examId) throws JsonProcessingException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        ExamStartResponse result = examService.startExam(examId, loginUser.getUser().getUserId().toString());
        if(!result.getAllowExam()){
            return new ResponseResult(502, "不允许参加该考试");
        }else{
            return new ResponseResult<>(200, result);
        }
    }

    @GetMapping("/finish-exam")
    public ResponseResult finishExam(@RequestParam("examUUID") String examUUID) throws IOException {
        examService.finishExam(examUUID);
        return new ResponseResult(200, "");
    }
}
