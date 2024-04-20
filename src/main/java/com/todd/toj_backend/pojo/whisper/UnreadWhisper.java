package com.todd.toj_backend.pojo.whisper;

import lombok.Data;

@Data
public class UnreadWhisper {
    Integer count;
    String sendUserId;
    String sendUserNickname;
}
