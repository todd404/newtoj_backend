package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.course.AddCourseRequest;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @GetMapping("/is-course-enroll")
    public ResponseResult getIsCourseEnroll(@RequestParam("courseId") String courseId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        AttendCourse attendCourse = new AttendCourse();
        attendCourse.setCourseId(courseId);
        attendCourse.setUserId(loginUser.getUser().getUserId().toString());

        return new ResponseResult(200, courseService.isCourseEnroll(attendCourse));
    }

    @PostMapping("/add-course")
    public ResponseResult addCourse(@RequestBody AddCourseRequest addCourseRequest){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }
        addCourseRequest.setUserId(loginUser.getUser().getUserId().toString());
        courseService.addCourse(addCourseRequest);

        return new ResponseResult<>(200, "");
    }

    @PostMapping("/add-course-enroll")
    public ResponseResult addCourseEnroll(@RequestBody AttendCourse attendCourse){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        attendCourse.setUserId(loginUser.getUser().getUserId().toString());
        courseService.addCourseEnroll(attendCourse);
        return new ResponseResult(200, "");
    }

    @PostMapping("/delete-course")
    public ResponseResult deleteCourse(@RequestBody Course course){
        int result = courseService.deleteCourse(course.getId().toString());
        if(result == 0){
            return new ResponseResult<>(500, "");
        }else{
            return new ResponseResult<>(200, "");
        }
    }

    @PostMapping("/upload-course-file")
    public ResponseResult uploadCourseFile(@RequestParam("file") MultipartFile file, @RequestParam("courseId") String courseId) throws IOException {
        Boolean result = courseService.uploadCourseFile(file, courseId);

        if(result){
            return new ResponseResult<>(200, "");
        }else{
            return new ResponseResult(500, "");
        }
    }

    @PostMapping("/delete-course-file")
    public ResponseResult deleteCourseFile(@RequestBody CourseFile courseFile){
        Boolean result = courseService.deleteCourseFile(courseFile.getId().toString(), courseFile.getCourseId());

        if(result){
            return new ResponseResult(200, "");
        }else{
            return new ResponseResult(500, "");
        }
    }

    @GetMapping("/my-course-list")
    public ResponseResult getUserCourseList(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }
        var result = courseService.getOwnCourseList(loginUser.getUser().getUserId().toString());

        return new ResponseResult(200, result);
    }

    @GetMapping("/course-file-list")
    public ResponseResult getCourseFileList(@RequestParam("courseId") String courseId){
        var result = courseService.getCourseFileList(courseId);

        return new ResponseResult(200, result);
    }

    @PostMapping("/remove-course-enroll")
    public ResponseResult removeCourseEnroll(@RequestBody AttendCourse attendCourse){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;
        if(loginUser == null){
            return new ResponseResult<>(403, "未登录");
        }

        attendCourse.setUserId(loginUser.getUser().getUserId().toString());
        courseService.deleteCourseEnroll(attendCourse);
        return new ResponseResult(200, "");
    }
}
