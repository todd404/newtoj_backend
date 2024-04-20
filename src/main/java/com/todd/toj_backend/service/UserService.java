package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.user.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserInfo getUserInfo(String username);
}
