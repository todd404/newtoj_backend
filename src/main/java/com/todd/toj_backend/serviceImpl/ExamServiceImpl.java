package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.ExamMapper;
import com.todd.toj_backend.pojo.exam.CourseExam;
import com.todd.toj_backend.pojo.exam.Exam;
import com.todd.toj_backend.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamMapper examMapper;
    @Override
    public Integer addExam(Exam exam) {
        return examMapper.insertExam(exam);
    }

    @Override
    public Integer addCourseExam(CourseExam courseExam) {
        return examMapper.insertCourseExam(courseExam);
    }

    @Override
    public List<Exam> getExamList(String userId) {
        return examMapper.queryExamList(userId);
    }

    @Override
    public List<CourseExam> getCourseExamList(String courseId) {
        return examMapper.queryCourseExam(courseId);
    }

    @Override
    public Integer deleteCourseExam(CourseExam courseExam) {
        return examMapper.deleteCourseExam(courseExam);
    }

}
