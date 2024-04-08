package com.todd.toj_backend.pojo.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class CommentUser {
    @JsonIgnore
    String userId;
    String username;
    String avatar;
    String homeLink;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setHomeLink(String homeLink) {
        this.homeLink = homeLink;
    }

    public String getAvatar() {
        //return "/files/avatar/" + userId;
        return "https://static.juzicon.com/user/avatar-bf22291e-ea5c-4280-850d-88bc288fcf5d-220408002256-ZBQQ.jpeg";
    }

    public String getHomeLink() {
        return "/user/" + userId;
    }
}
