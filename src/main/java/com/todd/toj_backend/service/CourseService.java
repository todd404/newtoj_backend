package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.course.AddCourseRequest;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CourseService {
    List<Course> getAllCourseList();
    Boolean isCourseEnroll(AttendCourse attendCourse);
    Integer addCourseEnroll(AttendCourse attendCourse);
    Course getCourse(String courseId);
    void addCourse(AddCourseRequest addCourseRequest);
    Integer deleteCourse(String courseId);
    boolean uploadCourseFile(MultipartFile file, String courseId) throws IOException;
    List<Course> getOwnCourseList(String userId);
    List<CourseFile> getCourseFileList(String courseId);
    boolean deleteCourseFile(String courseFileId, String courseId);
    void deleteAllCourseFile(String courseId);
    Integer deleteCourseEnroll(AttendCourse attendCourse);
}
