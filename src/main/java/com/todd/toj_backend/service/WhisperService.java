package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WhisperService {
    List<Whisper> getWhisperList(String userId, String otherId);
    List<UnreadWhisper> getUnreadWhisper(String userId);
    Integer sendWhisper(Whisper whisper);
    Integer readAllWhisper(Whisper whisper);
}
