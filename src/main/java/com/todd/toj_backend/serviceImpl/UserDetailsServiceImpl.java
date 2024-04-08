package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.UserMapper;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.user.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isBlank(username)){
            throw new RuntimeException("请输入用户名");
        }

        User user = userMapper.queryUserByUsername(username);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名不存在");
        }

        return new LoginUser(user);
    }
}
