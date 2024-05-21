package com.todd.toj_backend;

import com.todd.toj_backend.mapper.CommentMapper;
import com.todd.toj_backend.mapper.ProblemMapper;
import com.todd.toj_backend.mapper.WhisperMapper;
import com.todd.toj_backend.pojo.TypeMap;
import com.todd.toj_backend.pojo.problem.ProblemsetItem;
import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import com.todd.toj_backend.pojo.whisper.WhisperHistory;
import com.todd.toj_backend.service.WhisperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MapperTest {

   @Autowired
    WhisperService whisperService;

   @Autowired
    CommentMapper commentMapper;

   @Autowired
    TypeMap typeMap;

    @Test
    void whisperHistoryTest(){
        List<WhisperHistory> list = whisperService.getWhisperHistoryList(2);
        return;
    }

    @Test
    void commentLikeList(){
        List<Integer> list = commentMapper.queryCommentLikeList("1", "2");
        return;
    }

    @Test
    void typeMapTest(){
        System.out.println(typeMap.getCppTypeMap().get("int[][]"));
    }
}
