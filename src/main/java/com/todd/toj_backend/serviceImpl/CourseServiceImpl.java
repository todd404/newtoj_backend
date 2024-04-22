package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.CourseMapper;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import com.todd.toj_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourseList() {
        return courseMapper.queryAllCourse();
    }

    @Override
    public Course getCourse(String courseId) {
        return courseMapper.queryCourse(courseId);
    }

    @Override
    public List<Course> getCourseList(String userId) {
        return courseMapper.queryCourseUserIdLimit(userId);
    }

    @Override
    public List<CourseFile> getCourseFileList(String courseId) {
        return courseMapper.queryCourseFileList(courseId);
    }
}
