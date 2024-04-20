package com.todd.toj_backend;

import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mapper.WhisperMapper;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MapperTest {
    @Autowired
    WhisperMapper whisperMapper;

    @Autowired
    ProblemMapper problemMapper;

    @Test
    void queryWhisper(){
        String userId = "2";

        List<Whisper> whisperList = whisperMapper.queryWhispers("2", "1");

        return;
    }

    @Test
    void queryUnreadWhisper(){
        List<UnreadWhisper> unreadWhisper = whisperMapper.queryUnreadWhisper("2");
        return;
    }

    @Test
    void insertTags(){
        String problemId = "1";
        List<String> tags = Arrays.asList("简单问题");

        problemMapper.insertProblemTags(problemId, tags);
    }

    @Test
    void getProblemset(){
        List<ProblemsetItem> problemset = problemMapper.queryProblemset();

        return;
    }
}
