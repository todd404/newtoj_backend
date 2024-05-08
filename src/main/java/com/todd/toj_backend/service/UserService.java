package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.pojo.user.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUserList();
    UserInfo getUserInfo(String username);
    Boolean isUsernameRepeat(String username);
    Boolean isNicknameRepeat(String nickname);
    Integer addUser(User user);
}
