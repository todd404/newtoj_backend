package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import com.todd.toj_backend.utils.JacksonTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChoiceProblemMapper {
    @Select("select * from choice_problem where id = #{problemId}")
    @Result(column = "choice_problem_list", property = "choiceProblemList", typeHandler = JacksonTypeHandler.class)
    ChoiceProblemDao queryChoiceProblem(@Param("problemId") String problemId);

    @Select("select * from choice_problem where user_id = #{userId}")
    @Result(column = "choice_problem_list", property = "choiceProblemList", typeHandler = JacksonTypeHandler.class)
    List<ChoiceProblemDao> queryOwnChoiceProblem(@Param("userId") String userId);

    @Insert("insert into choice_problem (title, user_id, choice_problem_list) VALUES " +
            "(#{title}, #{userId}, #{choiceProblemList, typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insetChoiceProblem(ChoiceProblemDao choiceProblemDao);
}
