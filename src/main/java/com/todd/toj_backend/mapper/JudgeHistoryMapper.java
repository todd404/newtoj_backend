package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.judge.JudgeHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JudgeHistoryMapper {
    @Insert("insert into judge_history (problem_id, user_id, is_pass, status, time_used, memory_used) VALUES " +
            "(#{problemId}, #{userId}, #{isPass}, #{status}, #{timeUsed}, #{memoryUsed})")
    public Integer insertJudgeHistory(JudgeHistory judgeHistory);
}
