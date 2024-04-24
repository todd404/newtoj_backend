package com.todd.toj_backend.controller;

import com.todd.toj_backend.mapper.ExamMapper;
import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.exam.CourseExam;
import com.todd.toj_backend.pojo.exam.Exam;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamController {
    @Autowired
    ExamService examService;

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

    @GetMapping("/my-exam-list")
    public ResponseResult myExamList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        if(loginUser == null){
            return new ResponseResult(403, "未登录");
        }

        List<Exam> myExamList = examService.getExamList(loginUser.getUser().getUserId().toString());
        return new ResponseResult(200, myExamList);
    }

    @GetMapping("/course-exam-list")
    public ResponseResult courseExamList(@RequestParam("courseId") String courseId){
        return new ResponseResult(200, examService.getCourseExamList(courseId));
    }

    @PostMapping("/delete-course-exam")
    public ResponseResult removeCourseExam(@RequestBody CourseExam courseExam){
        int result = examService.deleteCourseExam(courseExam);

        if(result == 0){
            return new ResponseResult<>(500, "");
        }
        return new ResponseResult<>(200, "");
    }
}
