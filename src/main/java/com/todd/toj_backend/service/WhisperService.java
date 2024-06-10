package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.whisper.GroupWhisper;
import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import com.todd.toj_backend.pojo.whisper.WhisperHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhisperService {
    List<Whisper> getWhisperList(String userId, String otherId);
    List<UnreadWhisper> getUnreadWhisper(String userId);
    List<WhisperHistory> getWhisperHistoryList(Integer userId);
    Integer sendWhisper(Whisper whisper);
    Boolean sendGroupWhisper(GroupWhisper groupWhisper, String userId);
    Integer readAllWhisper(Whisper whisper);
}
