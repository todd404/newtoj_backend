package com.todd.toj_backend.pojo.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
        return "http://localhost/file/avatar/" + getUserId() + ".png";
    }

    public String getHomeLink() {
        return "/user/" + userId;
    }
}
