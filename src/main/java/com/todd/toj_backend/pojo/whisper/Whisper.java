package com.todd.toj_backend.pojo.whisper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Whisper {
    Integer id;
    String sendUserId;
    String receiveUserId;
    String content;
    @JsonIgnore
    Boolean isRead;
    String createAt;
}
