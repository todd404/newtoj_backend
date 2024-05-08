package com.todd.toj_backend.controller;

import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.comment.Comment;
import com.todd.toj_backend.pojo.user.LoginUser;
import com.todd.toj_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/comments")
    public ResponseResult comments(@RequestParam("problem_id") String problemId){
        List<Comment> comments = commentService.getAllCommentsByProblemId(problemId);
        return new ResponseResult(200, "success", comments);
    }

    @PostMapping("/submit_comment")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseResult submitComment(@RequestBody Comment comment){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        comment.setUid(loginUser.getUser().getUserId().toString());
        Comment result = commentService.submitComment(comment);
        return new ResponseResult<>(200, "success", result);
    }

    @GetMapping("/like-list")
    public ResponseResult getCommentLikeList(@RequestParam("problemId") String problemId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        return new ResponseResult(200, commentService.getCommentLikeList(problemId, loginUser.getUser().getUserId()));
    }

    @PostMapping("/like_comment")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseResult likeComment(@RequestBody Comment comment){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        comment.setUid(loginUser.getUser().getUserId().toString());

        commentService.likeComment(comment);
        return new ResponseResult<>(200, "success");
    }
}
