package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.course.CourseExam;
import com.todd.toj_backend.pojo.exam.*;
import com.todd.toj_backend.pojo.job.JobExam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public interface ExamService {
    Exam getExam(String examId);
    List<ExamItem> getExamItemList(String examId);
    List<Exam> getOwnExamList(String userId);
    List<CourseExam> getCourseExamList(String courseId);
    List<JobExam> getJobExamList(String jobId);
    List<AttendExam> getAttendExamList(String userId);
    List<ExamScore> getExamScoreList(String examId);
    Integer addExam(Exam exam);
    Integer addCourseExam(CourseExam courseExam);
    Integer addJobExam(JobExam jobExam);
    Integer deleteCourseExam(CourseExam courseExam);
    Integer deleteJobExam(JobExam jobExam);

    ExamStartResponse startExam(String examId, String userId) throws JsonProcessingException;
    void finishExam(String examUUID) throws IOException;

    boolean getExamResultExcel(String examId, OutputStream outputStream) throws IOException;
}
