package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.user.User;

public interface LoginService {
    ResponseResult login(User user);
    ResponseResult logout();
}
