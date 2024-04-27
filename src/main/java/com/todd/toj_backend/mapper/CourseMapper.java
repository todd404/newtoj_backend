package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.course.AddCourseRequest;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("select * from course inner join user on user.user_id = course.user_id")
    List<Course> queryAllCourse();

    @Select("select * from user_course_enrollment where user_id = #{userId}")
    List<AttendCourse> queryAllAttendCourse(@Param("userId") String userId);

    @Select("select id, course.user_id, title, nickname from course " +
            "inner join user on user.user_id = course.user_id " +
            "where id = #{courseId}")
    Course queryCourse(@Param("courseId") String courseId);

    @Select("select * from user_course_enrollment where user_id = #{userId} and course_id = #{courseId}")
    Integer queryIsCourseEnroll(AttendCourse attendCourse);

    @Select("select * from course_file where id = #{id}")
    CourseFile queryCourseFile(@Param("id") String courseFileId);

    @Select("select * from course where user_id = #{userId}")
    List<Course> queryCourseUserIdLimit(@Param("userId") String userId);

    @Select("select * from course_file where course_id = #{courseId}")
    List<CourseFile> queryCourseFileList(@Param("courseId") String courseId);

    @Insert("insert into course (user_id, title) values " +
            "(#{userId}, #{title})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertCourse(AddCourseRequest addCourseRequest);

    @Insert("insert ignore into user_course_enrollment (user_id, course_id) " +
            "values (#{userId}, #{courseId})")
    Integer insertCourseEnroll(AttendCourse attendCourse);

    @Insert("insert ignore into course_file (course_id, file, file_type) VALUES " +
            "(#{courseId}, #{file}, #{fileType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertCourseFile(CourseFile courseFile);

    @Delete("delete from course where id = #{courseId}")
    Integer deleteCourse(@Param("courseId") String courseId);

    @Delete("delete from user_course_enrollment where user_id = #{userId} and course_id = #{courseId}")
    Integer deleteCourseEnroll(AttendCourse attendCourse);

    @Delete("delete from course_file where id = #{fileId}")
    Integer deleteCourseFile(@Param("fileId") String fileId);

    @Delete("delete from course_file where course_id = #{courseId}")
    Integer deleteAllCourseFile(@Param("courseId") String courseId);
}
