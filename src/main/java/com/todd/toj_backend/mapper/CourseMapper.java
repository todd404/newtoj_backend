package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("select * from course inner join user on user.user_id = course.user_id")
    List<Course> queryAllCourse();

    @Select("select id, course.user_id, title, nickname from course " +
            "inner join user on user.user_id = course.user_id " +
            "where id = #{courseId}")
    Course queryCourse(@Param("courseId") String courseId);

    @Select("select * from course where user_id = #{userId}")
    List<Course> queryCourseUserIdLimit(@Param("userId") String userId);

    @Select("select * from course_file where course_id = #{courseId}")
    List<CourseFile> queryCourseFileList(@Param("courseId") String courseId);
}
