package com.todd.toj_backend.pojo.user;

import lombok.Data;

@Data
public class UserInfo {
    Integer userId;
    String username;
    String nickname;
    String avatar;
    String role;
    Integer ranking;
}
