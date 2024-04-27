package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.job.AttendJob;
import com.todd.toj_backend.pojo.job.Job;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobMapper {
    @Select("select * from job where id = #{jobId}")
    Job queryJob(String jobId);

    @Select("select * from job")
    List<Job> queryAllJob();

    @Select("select job_id from user_job_enrollment where user_id=#{userId}")
    List<Integer> queryJobEnroll(@Param("userId") Integer userId);

    @Select("select * from job where user_id = #{userId}")
    List<Job> queryOwnJob(@Param("userId") Integer userId);

    @Select("select * from user_job_enrollment where user_id = #{userId}")
    List<AttendJob> queryAllAttendJob(@Param("userId") String userId);

    @Insert("insert into job (user_id, title, `require`, salary, start_date, end_date) " +
            "VALUES (#{userId}, #{title}, #{require}, #{salary}, #{startDate}, #{endDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertJob(Job job);

    @Insert("insert ignore into user_job_enrollment (user_id, job_id) " +
            "VALUES (#{userId}, #{jobId})")
    Integer insertJobEnroll(@Param("userId") Integer userId, @Param("jobId") Integer jobId);

    @Delete("delete from user_job_enrollment where user_id = #{userId} and job_id = #{jobId}")
    Integer deleteJobEnroll(@Param("userId") Integer userId, @Param("jobId") Integer jobId);
}
