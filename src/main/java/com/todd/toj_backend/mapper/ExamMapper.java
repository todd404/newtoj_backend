package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.exam.CourseExam;
import com.todd.toj_backend.pojo.exam.Exam;
import com.todd.toj_backend.pojo.exam.ExamItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {
    @Select("select * from exam where user_id = #{userId}")
    List<Exam> queryExamList(String userId);

    @Select("select * from course_exam where course_id = #{courseId}")
    List<CourseExam> queryCourseExam(String courseId);

    @Insert("insert into exam (user_id, title, start_time, end_time, time_limit, exam_item_list) VALUES " +
            "(#{userId}, #{title}, #{startTime}, #{endTime}, #{timeLimit}, #{examItemList, typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insertExam(Exam exam);

    @Insert("insert into course_exam (course_id, exam_id) " +
            "values (#{courseId}, #{examId})")
    Integer insertCourseExam(CourseExam courseExam);

    @Delete("delete from course_exam where course_id = #{courseId} and exam_id = #{examId}")
    Integer deleteCourseExam(CourseExam courseExam);
}
