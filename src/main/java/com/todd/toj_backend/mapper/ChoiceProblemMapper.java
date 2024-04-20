package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.choice_problem.ChoiceProblemDao;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChoiceProblemMapper {
    @Insert("insert into choice_problem (title, user_id, choice_problem_list) VALUES " +
            "(#{title}, #{userId}, #{choiceProblemList, typeHandler=com.todd.toj_backend.utils.JacksonTypeHandler})")
    Integer insetChoiceProblem(ChoiceProblemDao choiceProblemDao);
}
