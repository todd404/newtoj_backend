package com.todd.toj_backend.pojo.whisper;

import lombok.Data;

import java.util.Date;

@Data
public class WhisperHistory {
    Integer userId;
    String nickname;
    Integer unreadCount;
    Date lastMessageDate;
    String lastMessage;
}
