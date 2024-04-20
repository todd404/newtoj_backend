package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.UserMapper;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.pojo.user.UserInfo;
import com.todd.toj_backend.pojo.user.UserRoleList;
import com.todd.toj_backend.service.UserService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisCache redisCache;

    @Override
    public UserInfo getUserInfo(String username) {
        LoginUser loginUser =  redisCache.getCacheObject("login:" + username);
        User user = loginUser.getUser();

        if(user == null)
            return null;

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar("/file/avatar/%s.png".formatted(user.getUserId()));
        userInfo.setRole(UserRoleList.getUserRoleArray()[user.getRole()]);

        return userInfo;
    }
}
