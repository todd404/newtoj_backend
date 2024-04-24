package com.todd.toj_backend.service;

import com.todd.toj_backend.mapper.ExamMapper;
import com.todd.toj_backend.pojo.exam.CourseExam;
import com.todd.toj_backend.pojo.exam.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExamService {
    Integer addExam(Exam exam);
    Integer addCourseExam(CourseExam courseExam);
    List<Exam> getExamList(String userId);
    List<CourseExam> getCourseExamList(String courseId);
    Integer deleteCourseExam(CourseExam courseExam);
}
