package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.comment.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<Comment> getAllCommentsByProblemId(String problemId);
    List<Integer> getCommentLikeList(String problemId, Integer userId);
    Comment submitComment(Comment comment);
    Integer likeComment(Comment comment);
}
