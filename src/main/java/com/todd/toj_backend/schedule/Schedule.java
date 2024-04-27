package com.todd.toj_backend.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todd.toj_backend.pojo.exam.ExamProcess;
import com.todd.toj_backend.service.ExamService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Async
public class Schedule {
    @Autowired
    RedisCache redisCache;

    @Autowired
    ExamService examService;

    @Scheduled(fixedRate = 1000)
    public void checkExamTimeLimit() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Collection<String> examUUIDList = redisCache.keys("exam:*");
        for(String uuid : examUUIDList){
            String msg = redisCache.getCacheObject(uuid);
            if(msg == null)
                continue;

            ExamProcess examProcess = objectMapper.readValue(msg, ExamProcess.class);
            Long now = new Date().getTime();
            if(now > examProcess.getStartTime().getTime() + examProcess.getTimeLimit() * 1000 * 60){
                examService.finishExam(uuid.replace("exam:", ""));
            }
        }
    }

}
