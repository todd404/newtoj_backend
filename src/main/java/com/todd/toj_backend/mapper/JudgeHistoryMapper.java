package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.judge.JudgeHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JudgeHistoryMapper {
    @Select("SELECT " +
            "    ROUND(SUM(CASE WHEN is_pass = 1 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) AS pass_rate " +
            "FROM " +
            "    judge_history " +
            "where " +
            "    problem_id = #{problemId} " +
            "GROUP BY " +
            "    problem_id;")
    Double queryProblemPassRate(String problemId);

    @Select("SELECT " +
            "    MAX(CASE WHEN is_pass = 1 THEN true ELSE false END) AS passed " +
            "FROM " +
            "    judge_history " +
            "where " +
            "    user_id = #{userID} and problem_id = #{problemId} " +
            "GROUP BY \n" +
            "    problem_id, user_id;")
    Boolean queryIsProblemPass(@Param("problemId") String problemId, @Param("userID") String userId);

    @Select("select * from judge_history where problem_id = #{problemId} and user_id=#{userId}")
    List<JudgeHistory> queryJudgeHistory(@Param("problemId") String problemId, @Param("userId") String userId);

    @Insert("insert into judge_history (problem_id, user_id, is_pass, status, time_used, memory_used) VALUES " +
            "(#{problemId}, #{userId}, #{isPass}, #{status}, #{timeUsed}, #{memoryUsed})")
    public Integer insertJudgeHistory(JudgeHistory judgeHistory);


}
