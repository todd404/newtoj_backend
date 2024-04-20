package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.pojo.user.UserInfo;
import com.todd.toj_backend.service.LoginService;
import com.todd.toj_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/userinfo")
    public ResponseResult userinfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        UserInfo userInfo = userService.getUserInfo(loginUser.getUsername());
        if(userInfo == null){
            return new ResponseResult(500, "获取用户信息失败");
        }

        return new ResponseResult<>(200, userInfo);
    }
}
