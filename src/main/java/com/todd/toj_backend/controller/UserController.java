package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.user.User;
import com.todd.toj_backend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    LoginService loginService;

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
    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }


}
