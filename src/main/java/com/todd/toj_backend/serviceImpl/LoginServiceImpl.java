package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.service.LoginService;
import com.todd.toj_backend.utils.JwtTokenUtil;
import com.todd.toj_backend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ResponseResult login(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authentication);
        if(authenticate == null){
            throw new RuntimeException("用户名密码错误");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String username = loginUser.getUser().getUsername();

        String jwt = jwtTokenUtil.generateToken(loginUser);
        redisCache.setCacheObject("login:" + username, loginUser);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult(200, map);
    }

    @Override
    public ResponseResult logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal != null){
            LoginUser loginUser = (LoginUser) principal;
            redisCache.deleteObject("login:"+loginUser.getUsername());
            return new ResponseResult<>(200, "success");
        }

        return new ResponseResult<>(401, "未登录");
    }
}
