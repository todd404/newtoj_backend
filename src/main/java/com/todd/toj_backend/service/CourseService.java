package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    List<Course> getAllCourseList();
    Course getCourse(String courseId);
    List<Course> getCourseList(String userId);
    List<CourseFile> getCourseFileList(String courseId);
}
