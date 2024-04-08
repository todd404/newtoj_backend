package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.utils.JsonTypeHandler;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProblemMapper {
    @Select("select * from problem where id=#{id}")
    @Result(column = "problem_config", property = "problemConfig", typeHandler = JsonTypeHandler.class)
    Problem queryProblem(String id);

    @Insert("insert into " +
            "problem (problem_config, title, content) " +
            "VALUES (#{problemConfig, typeHandler=com.todd.toj_backend.utils.JsonTypeHandler}, #{title}, #{content})")
    Integer insertProblem(Problem problem);
}
