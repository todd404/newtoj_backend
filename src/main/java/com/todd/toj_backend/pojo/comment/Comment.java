package com.todd.toj_backend.pojo.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Comment {
    String id;
    String uid;
    String parentId;
    String problemId;
    String content;
    String createTime;

    CommentUser user;

    Integer likes = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    CommentReplay replay;

}
