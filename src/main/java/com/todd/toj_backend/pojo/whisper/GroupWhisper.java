package com.todd.toj_backend.pojo.whisper;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupWhisper {
    String whisperContent;
    List<String> recieveIdList = new ArrayList<>();
}
