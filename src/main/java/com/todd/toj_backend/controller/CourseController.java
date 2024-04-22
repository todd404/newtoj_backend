package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/all-course")
    public ResponseResult getCourseList(){
        var result = courseService.getAllCourseList();

        if(result == null){
            return new ResponseResult(500, "内部错误");
        }

        return new ResponseResult(200, result);
    }

    @GetMapping("/get-course")
    public ResponseResult getCourse(@RequestParam("courseId") String courseId){
        var result = courseService.getCourse(courseId);

        if(result == null){
            return new ResponseResult(500, "内部错误");
        }

        return new ResponseResult(200, result);
    }

    @GetMapping("/user-course-list")
    public ResponseResult getUserCourseList(@RequestParam("userId") String userId){
        var result = courseService.getCourseList(userId);

        return new ResponseResult(200, result);
    }

    @GetMapping("/course-file-list")
    public ResponseResult getCourseFileList(@RequestParam("courseId") String courseId){
        var result = courseService.getCourseFileList(courseId);

        return new ResponseResult(200, result);
    }
}
