package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.UserMapper;
import com.todd.toj_backend.mapper.WhisperMapper;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import com.todd.toj_backend.pojo.whisper.WhisperHistory;
import com.todd.toj_backend.service.WhisperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class WhisperServiceImpl implements WhisperService {
    @Autowired
    WhisperMapper whisperMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<Whisper> getWhisperList(String userId, String otherId) {
        return whisperMapper.queryWhispers(userId, otherId);
    }

    @Override
    public List<UnreadWhisper> getUnreadWhisper(String userId) {
        List<UnreadWhisper> result = whisperMapper.queryUnreadWhisper(userId);
        for(var w : result){
            User user = userMapper.queryUserByUserId(w.getSendUserId());
            if(user == null){
                result.remove(w);
                continue;
            }

            w.setSendUserNickname(user.getNickname());
        }

        return result;
    }

    @Override
    public List<WhisperHistory> getWhisperHistoryList(Integer userId) {
        List<WhisperHistory> whisperHistoryList;

        whisperHistoryList = whisperMapper.queryReceiveWhisperHistory(userId.toString());
        for(var whisperHis : whisperHistoryList){
            String lastMessage = whisperMapper.queryLastMessage(whisperHis.getUserId(), userId);
            whisperHis.setLastMessage(lastMessage);
        }

        List<WhisperHistory> sendWhipserHistoryList = whisperMapper.querySendWhisperHistory(userId.toString());
        for(var sendWhisper : sendWhipserHistoryList){
            String lastMessage = whisperMapper.queryLastMessage(userId, sendWhisper.getUserId());
            sendWhisper.setLastMessage(lastMessage);
            boolean isFound = false;
            for(var whisper : whisperHistoryList){
                if(sendWhisper.getUserId() == whisper.getUserId()){
                    isFound = true;
                    if(sendWhisper.getLastMessageDate().after(whisper.getLastMessageDate())){
                        whisper.setLastMessage(lastMessage);
                        whisper.setLastMessageDate(sendWhisper.getLastMessageDate());
                    }
                    break;
                }
            }

            if(!isFound){
                whisperHistoryList.add(sendWhisper);
            }
        }

        return whisperHistoryList;
    }

    @Override
    public Integer sendWhisper(Whisper whisper) {
        return whisperMapper.insertWhisper(whisper);
    }

    @Override
    public Integer readAllWhisper(Whisper whisper) {
        return whisperMapper.readAllWhisper(whisper);
    }
}
