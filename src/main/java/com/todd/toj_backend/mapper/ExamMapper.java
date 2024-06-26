package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.course.CourseExam;
import com.todd.toj_backend.pojo.exam.*;
import com.todd.toj_backend.pojo.job.JobExam;
import com.todd.toj_backend.utils.JacksonTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {
    @Select("select * from exam where id = #{examId}")
    @Result(column = "exam_item_list", property = "examItemList", typeHandler = JacksonTypeHandler.class)
    Exam queryExam(String examId);

    @Select("select id, exam_id, user_id, score, timeUsed, finish_at, answer_collect " +
            "from exam_result where exam_id = #{examId} and user_id = #{userId}")
    @Result(column = "answer_collect", property = "answerCollect", typeHandler = JacksonTypeHandler.class)
    ExamResult queryExamResult(@Param("examId") String examId, @Param("userId") String userId);

    @Select("select id, exam_id, user_id, score, timeUsed, finish_at, answer_collect " +
            "from exam_result where exam_id = #{examId}")
    @Result(column = "answer_collect", property = "answerCollect", typeHandler = JacksonTypeHandler.class)
    List<ExamResult> queryExamResultList(@Param("examId") String examId);

    @Select("select exam.id, title, start_time, end_time, time_limit, score " +
            "from exam left join exam_result er on exam.id = er.exam_id and er.user_id = #{userId} " +
            "where exam.id = #{examId}")
    ExamWithScore queryExamWithScore(@Param("examId") String examId, @Param("userId") String userId);

    @Select("select * from exam where user_id = #{userId}")
    List<Exam> queryExamList(String userId);

    @Select("select * from course_exam where course_id = #{courseId}")
    List<CourseExam> queryCourseExam(String courseId);

    @Select("select * from job_exam where job_id = #{jobId}")
    List<JobExam> queryJobExam(String jobId);

    @Select("SELECT exam_result.id, exam_id, exam_result.user_id, user.username, user.nickname, score, timeUsed FROM exam_result " +
            "inner join user on exam_result.user_id =  user.user_id " +
            "where exam_id = #{examId}")
    List<ExamScore> queryExamScoreList(String examId);

    @Insert("insert into exam (user_id, title, start_time, end_time, time_limit, exam_item_list) VALUES " +
            "(#{userId}, #{title}, #{startTime}, #{endTime}, #{timeLimit}, #{examItemList, typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insertExam(Exam exam);

    @Insert("insert into course_exam (course_id, exam_id) " +
            "values (#{courseId}, #{examId})")
    Integer insertCourseExam(CourseExam courseExam);

    @Insert("insert into job_exam (job_id, exam_id) " +
            "values (#{jobId}, #{examId})")
    Integer insertJobExam(JobExam jobExam);

    @Insert("insert ignore into exam_result (exam_id, user_id, score, timeUsed, answer_collect) VALUES " +
            "(#{examId}, #{userId}, #{score}, #{timeUsed}, #{answerCollect, typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insertExamResult(ExamResult examResult);

    @Delete("delete from course_exam where course_id = #{courseId} and exam_id = #{examId}")
    Integer deleteCourseExam(CourseExam courseExam);

    @Delete("delete from job_exam where job_id = #{jobId} and exam_id = #{examId}")
    Integer deleteJobExam(JobExam jobExam);
}
