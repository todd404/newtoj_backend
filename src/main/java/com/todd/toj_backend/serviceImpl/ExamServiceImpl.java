package com.todd.toj_backend.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mapper.CourseMapper;
import com.todd.toj_backend.mapper.ExamMapper;
import com.todd.toj_backend.mapper.JobMapper;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseExam;
import com.todd.toj_backend.pojo.exam.*;
import com.todd.toj_backend.pojo.job.AttendJob;
import com.todd.toj_backend.pojo.job.Job;
import com.todd.toj_backend.pojo.job.JobExam;
import com.todd.toj_backend.service.ExamService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    JobMapper jobMapper;

    @Autowired
    ExamMapper examMapper;

    @Override
    public Exam getExam(String examId) {
        return examMapper.queryExam(examId);
    }

    @Override
    public List<ExamItem> getExamItemList(String examId) {
        return getExam(examId).getExamItemList();
    }

    @Override
    public List<Exam> getOwnExamList(String userId) {
        return examMapper.queryExamList(userId);
    }

    @Override
    public List<CourseExam> getCourseExamList(String courseId) {
        return examMapper.queryCourseExam(courseId);
    }

    @Override
    public List<JobExam> getJobExamList(String jobId) {
        return examMapper.queryJobExam(jobId);
    }

    @Override
    public List<AttendExam> getAttendExamList(String userId) {
        List<AttendExam> attendExamList = new ArrayList<>();
        List<AttendCourse> attendCourseList = courseMapper.queryAllAttendCourse(userId);
        List<AttendJob> attendJobList = jobMapper.queryAllAttendJob(userId);

        for(var attCourse : attendCourseList){
            AttendExam pushAttendExam = new AttendExam();
            pushAttendExam.setType("course");
            Course course = courseMapper.queryCourse(attCourse.getCourseId());
            pushAttendExam.setTitle(course.getTitle());

            List<CourseExam> courseExamList = examMapper.queryCourseExam(attCourse.getCourseId());
            List<ExamWithScore> attendExamItemList = new ArrayList<>();
            for(var courseExam : courseExamList){
                ExamWithScore examWithScore = examMapper.queryExamWithScore(courseExam.getExamId(), userId);
                attendExamItemList.add(examWithScore);
            }

            pushAttendExam.setAttendExamItemList(attendExamItemList);

            attendExamList.add(pushAttendExam);
        }

        for(var attJob : attendJobList){
            AttendExam pushAttendExam = new AttendExam();
            pushAttendExam.setType("job");
            Job job = jobMapper.queryJob(attJob.getJobId().toString());
            pushAttendExam.setTitle(job.getTitle());

            List<JobExam> jobExamList = examMapper.queryJobExam(String.valueOf(attJob.getJobId()));
            List<ExamWithScore> attendExamItemList = new ArrayList<>();
            for(var jobExam : jobExamList){
                ExamWithScore examWithScore = examMapper.queryExamWithScore(String.valueOf(jobExam.getExamId()), userId);
                attendExamItemList.add(examWithScore);
            }

            pushAttendExam.setAttendExamItemList(attendExamItemList);

            attendExamList.add(pushAttendExam);
        }

        return attendExamList;
    }

    @Override
    public Integer addExam(Exam exam) {
        return examMapper.insertExam(exam);
    }

    @Override
    public Integer addCourseExam(CourseExam courseExam) {
        return examMapper.insertCourseExam(courseExam);
    }

    @Override
    public Integer addJobExam(JobExam jobExam) {
        return examMapper.insertJobExam(jobExam);
    }

    @Override
    public Integer deleteCourseExam(CourseExam courseExam) {
        return examMapper.deleteCourseExam(courseExam);
    }

    @Override
    public Integer deleteJobExam(JobExam jobExam) {
        return examMapper.deleteJobExam(jobExam);
    }

    @Override
    public ExamStartResponse startExam(String examId, String userId) throws JsonProcessingException {
        ExamStartResponse examStartResponse = new ExamStartResponse();
        examStartResponse.setAllowExam(false);

        Exam exam = getExam(examId);
        if(new Date().after(exam.getEndTime())){
            examStartResponse.setAllowExam(false);
            return examStartResponse;
        }else if (examMapper.queryExamResult(examId, userId) != null){
            examStartResponse.setAllowExam(false);
            return examStartResponse;
        }else{
            examStartResponse.setAllowExam(true);
        }

        ExamProcess examProcess = new ExamProcess();
        String uuid = UUID.randomUUID().toString();
        examProcess.setUuid(uuid);
        examProcess.setUserId(userId);
        examProcess.setExamId(examId);
        examProcess.setStartTime(new Date());
        examProcess.setTimeLimit(exam.getTimeLimit());
        examProcess.setExamItemList(exam.getExamItemList());

        List<Float> scoreList = new ArrayList<>();
        for(int i = 0; i < exam.getExamItemList().size(); i++){
            scoreList.add(0.0F);
        }
        examProcess.setScoreList(scoreList);

        ObjectMapper objectMapper = new ObjectMapper();
        redisCache.setCacheObject("exam:" + uuid, objectMapper.writeValueAsString(examProcess));

        examStartResponse.setUuid(uuid);
        examStartResponse.setTimeLimit(exam.getTimeLimit());
        return examStartResponse;
    }

    @Override
    public void finishExam(String examUUID) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg = redisCache.getCacheObject("exam:" + examUUID);
        if(msg == null){
            return;
        }

        ExamProcess examProcess = objectMapper.readValue(msg, ExamProcess.class);
        ExamResult examResult = new ExamResult();
        examResult.setExamId(Integer.valueOf(examProcess.getExamId()));
        examResult.setUserId(Integer.valueOf(examProcess.getUserId()));
        Date startTime = examProcess.getStartTime();
        long timeUsedMS = new Date().getTime() - startTime.getTime();
        int timeUsedSecond = (int) (timeUsedMS / 1000);
        examResult.setTimeUsed(timeUsedSecond);
        Float totalScore = 0f;
        for(float score : examProcess.getScoreList()){
            totalScore += score;
        }
        examResult.setScore(totalScore);

        examMapper.insertExamResult(examResult);
        redisCache.deleteObject("exam:" + examUUID);
    }

}
