package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.UserMapper;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.pojo.user.UserInfo;
import com.todd.toj_backend.pojo.user.UserRoleList;
import com.todd.toj_backend.service.UserService;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> getAllUserList() {
        return userMapper.queryAllUserList();
    }

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
        userInfo.setRanking(user.getRanking());

        return userInfo;
    }

    @Override
    public Boolean isUsernameRepeat(String username) {
        return userMapper.queryUserByUsername(username) != null;
    }

    @Override
    public Boolean isNicknameRepeat(String nickname) {
        return userMapper.queryUserByNickname(nickname) != null;
    }

    @Override
    public Integer addUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userMapper.insertUser(user);
    }

    @Override
    public Integer changeUserRole(User user) {
        return userMapper.updateUserRole(user);
    }
}
