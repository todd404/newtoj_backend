package com.todd.toj_backend.pojo.comment;

import lombok.Data;

import java.util.List;

@Data
public class CommentReplay {
    List<Comment> list;
    Integer total;

    public Integer getTotal() {
        return list.size();
    }
}
