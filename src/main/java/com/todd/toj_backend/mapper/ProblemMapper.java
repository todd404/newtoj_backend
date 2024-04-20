package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.problem.Problem;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.utils.JacksonTypeHandler;
import com.todd.toj_backend.utils.JsonTypeHandler;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProblemMapper {
    @Select("select * from problem where id=#{id}")
    @Result(column = "problem_config", property = "problemConfig", typeHandler = JsonTypeHandler.class)
    Problem queryProblem(String id);

    @Select("SELECT problem.id, title, content, problem_tags.tags " +
            "FROM new_oj.problem " +
            "left join problem_tags on problem.id = problem_tags.problem_id;")
    @Result(column = "tags", property = "tags", typeHandler = JacksonTypeHandler.class)
    List<ProblemsetItem> queryProblemset();

    @Insert("insert into " +
            "problem (problem_config, title, content) " +
            "VALUES (#{problemConfig, typeHandler=com.todd.toj_backend.utils.JsonTypeHandler}, #{title}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertProblem(Problem problem);

    @Insert("insert into problem_tags(problem_id, tags) values (#{problemId}, #{tags,typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insertProblemTags(@Param("problemId") String problemId, @Param("tags") List<String> tags);
}
