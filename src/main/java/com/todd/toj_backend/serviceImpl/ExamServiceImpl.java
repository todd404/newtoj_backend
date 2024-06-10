package com.todd.toj_backend.serviceImpl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.NullOutputStream;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.mapper.*;
import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseExam;
import com.todd.toj_backend.pojo.exam.*;
import com.todd.toj_backend.pojo.job.AttendJob;
import com.todd.toj_backend.pojo.job.Job;
import com.todd.toj_backend.pojo.job.JobExam;
import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemAnswer;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.service.ExamService;
import com.todd.toj_backend.utils.ExcelUtil;
import com.todd.toj_backend.utils.RedisCache;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    JobMapper jobMapper;

    @Autowired
    ExamMapper examMapper;

    @Autowired
    ChoiceProblemMapper choiceProblemMapper;

    @Autowired
    ProblemMapper problemMapper;

    @Value("${file-path.base-file-path}")
    String baseFilePath;

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
                ExamWithScore examWithScore = examMapper.queryExamWithScore(jobExam.getExamId().toString(), userId);
                attendExamItemList.add(examWithScore);
            }

            pushAttendExam.setAttendExamItemList(attendExamItemList);

            attendExamList.add(pushAttendExam);
        }

        return attendExamList;
    }

    @Override
    public List<ExamScore> getExamScoreList(String examId) {
        return examMapper.queryExamScoreList(examId);
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
    @Transactional
    public void finishExam(String examUUID) throws IOException {
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
        float totalScore = 0f;
        for(float score : examProcess.getScoreList()){
            totalScore += score;
        }
        examResult.setScore(totalScore);

        List<ChoiceProblemAnswer> choiceProblemAnswerList = new ArrayList<>();

        examProcess.getChoiceProblemAnswerMap().forEach((key, value)->{
            ChoiceProblemAnswer choiceProblemAnswer = new ChoiceProblemAnswer();
            choiceProblemAnswer.setChoiceProblemId(key);
            choiceProblemAnswer.setAnswerList(value);
            choiceProblemAnswerList.add(choiceProblemAnswer);
        });
        examResult.setAnswerCollect(choiceProblemAnswerList);

        examMapper.insertExamResult(examResult);
        redisCache.deleteObject("exam:" + examUUID);

        String examResultPath = baseFilePath + "/exam_result/exam_"
                + examProcess.getExamId() + "_user_"
                + examProcess.getUserId() + ".xlsm";
        FileUtil.copy(Paths.get(baseFilePath + "/template.xlsm"),
                Paths.get(examResultPath),
                StandardCopyOption.REPLACE_EXISTING);
        XSSFWorkbook workbook = new XSSFWorkbook(examResultPath);
        Sheet sheet = workbook.getSheetAt(0);
        sheet.createRow(0).createCell(0).setCellValue("完成时间：" + timeUsedSecond + "秒");
        for(var problem : examProcess.getExamItemList()){
            if(problem.getType().equals("choice")){
                ChoiceProblemDao choiceProblem = choiceProblemMapper.queryChoiceProblem(problem.getProblemId().toString());
                List<String> answerList = examProcess.getChoiceProblemAnswerMap().get(problem.getProblemId());
                if(answerList == null) continue;
                ExcelUtil.printChoiceProblemAnswer(sheet, choiceProblem.getTitle(), answerList);
            }else if(problem.getType().equals("program")){
                Problem programProblem = problemMapper.queryProblem(problem.getProblemId().toString());
                ProblemAnswer problemAnswer = examProcess.getProgramProblemAnswerMap().get(problem.getProblemId());
                if (problemAnswer == null) continue;
                ExcelUtil.printProgramProblemAnswer(sheet, programProblem.getTitle(), problemAnswer);
            }
        }

        workbook.write(new NullOutputStream());
        workbook.close();
    }

    @Override
    public boolean getExamResultExcel(String examId, OutputStream outputStream) throws IOException {
        List<ExamResult> examResultList = examMapper.queryExamResultList(examId);
        if(examResultList.isEmpty()) return false;

        Map<Integer, List<UserExamChoiceAnswer>> answerMap = new HashMap<>();

        for(ExamResult examResult : examResultList){
            String json = JSONUtil.toJsonStr(examResult.getAnswerCollect());
            List<ChoiceProblemAnswer> choiceProblemAnswerList = JSONUtil.toList(json, ChoiceProblemAnswer.class);
            for(ChoiceProblemAnswer choiceProblemAnswer : choiceProblemAnswerList){
                List<UserExamChoiceAnswer> userExamChoiceAnswerList = answerMap.computeIfAbsent(
                        choiceProblemAnswer.getChoiceProblemId(),
                        k -> new ArrayList<>()
                );

                UserExamChoiceAnswer userExamChoiceAnswer = new UserExamChoiceAnswer();
                User user = userMapper.queryUserByUserId(String.valueOf(examResult.getUserId()));
                userExamChoiceAnswer.setUserId(user.getUserId());
                userExamChoiceAnswer.setUsername(user.getUsername());
                userExamChoiceAnswer.setNickname(user.getNickname());
                userExamChoiceAnswer.setScore(examResult.getScore());
                userExamChoiceAnswer.setAnswerList(choiceProblemAnswer.getAnswerList());

                userExamChoiceAnswerList.add(userExamChoiceAnswer);
            }
        }

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        answerMap.forEach((key, value)->{
            Exam exam = examMapper.queryExam(key.toString());
            ExcelUtil.printChoiceAnswerList(sheet, exam.getTitle(), value);
        });

        workbook.write(outputStream);
        workbook.dispose();
        workbook.close();
        return true;
    }

}
