package com.todd.toj_backend.serviceImpl;

import com.todd.toj_backend.mapper.CommentMapper;
import com.todd.toj_backend.pojo.comment.Comment;
import com.todd.toj_backend.pojo.comment.CommentReplay;
import com.todd.toj_backend.pojo.comment.CommentUser;
import com.todd.toj_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getAllCommentsByProblemId(String problemId) {
        List<Comment> comments = commentMapper.queryParentComments(problemId);
        for(var c : comments){
            CommentReplay replay = new CommentReplay();
            replay.setList(commentMapper.querySubComments(c.getId()));
            c.setReplay(replay);
        }

        return comments;
    }

    @Override
    public Comment submitComment(Comment comment) {
        commentMapper.insertComment(comment);
        //查询出点赞数
        return commentMapper.queryComment(comment.getId());
    }

    @Override
    public Integer likeComment(Comment comment) {
        return commentMapper.likeComment(comment);
    }
}
