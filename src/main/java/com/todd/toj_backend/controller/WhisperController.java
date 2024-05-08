package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.whisper.Whisper;
import com.todd.toj_backend.pojo.whisper.WhisperHistory;
import com.todd.toj_backend.service.WhisperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WhisperController {
    @Autowired
    WhisperService whisperService;

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/whisper-list")
    ResponseResult whisperList(@RequestParam("other_id") String otherId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        var result = whisperService.getWhisperList(loginUser.getUser().getUserId().toString(), otherId);

        return new ResponseResult<>(200, result);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping("/send-whisper")
    ResponseResult sendWhisper(@RequestBody Whisper whisper){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        whisper.setSendUserId(loginUser.getUser().getUserId().toString());
        var result = whisperService.sendWhisper(whisper);
        if(result > 0){
            return new ResponseResult<>(200, "发送成功");
        }else{
            return new ResponseResult<>(500, "发送失败");
        }
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/unread-whisper")
    ResponseResult unreadWhisper(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        var result = whisperService.getUnreadWhisper(loginUser.getUser().getUserId().toString());
        return new ResponseResult<>(200, result);
    }

    @GetMapping("/whisper-history")
    ResponseResult whisperHistory(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        var result = whisperService.getWhisperHistoryList(loginUser.getUser().getUserId());
        return new ResponseResult(200, result);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping("/read-all-whisper")
    ResponseResult readAllWhisper(@RequestBody Whisper whisper){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        whisper.setReceiveUserId(loginUser.getUser().getUserId().toString());
        var result = whisperService.readAllWhisper(whisper);
        if(result > 0){
            return new ResponseResult<>(200, "成功");
        }else{
            return new ResponseResult<>(500, "失败");
        }
    }
}
