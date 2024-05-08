package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.comment.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("""
        select
	        c.id,
            c.parent_id,
            c.content,
            c.user_id,
            c.create_time,
            user.username,
            COALESCE(l_count.like_count, 0) AS like_count
        from
	        comment as c
        join
	        user on c.user_id = user.user_id
        left join
	        (SELECT comment_id, sum(is_like) AS like_count FROM comment_like GROUP BY comment_id) AS l_count ON l_count.comment_id = c.id
        where c.problem_id = #{problemId} and c.parent_id is NULL;
""")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "uid"),
            @Result(column = "parentId", property = "parent_id"),
            @Result(column = "content", property = "content"),
            @Result(column = "createTime", property = "create_time"),
            @Result(column = "user_id", property = "user.userId"),
            @Result(column = "username", property = "user.username"),
            @Result(column = "like_count", property = "likes")
    })
    List<Comment> queryParentComments(String problemId);

    @Select("""
        select
	        c.id,
            c.parent_id,
            c.content,
            c.user_id,
            c.create_time,
            user.username,
            COALESCE(l_count.like_count, 0) AS like_count
        from
	        comment as c
        join
	        user on c.user_id = user.user_id
        left join
	        (SELECT comment_id, sum(is_like) AS like_count FROM comment_like GROUP BY comment_id) AS l_count ON l_count.comment_id = c.id
        where c.parent_id = #{commentId};
""")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "uid"),
            @Result(column = "parentId", property = "parent_id"),
            @Result(column = "content", property = "content"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "user_id", property = "user.userId"),
            @Result(column = "username", property = "user.username"),
            @Result(column = "like_count", property = "likes")
    })
    List<Comment> querySubComments(String commentId);

    @Select("""
        select
	        c.id,
            c.parent_id,
            c.content,
            c.user_id,
            c.create_time,
            user.username,
            COALESCE(l_count.like_count, 0) AS like_count
        from
	        comment as c
        join
	        user on c.user_id = user.user_id
        left join
	        (SELECT comment_id, sum(is_like) AS like_count FROM comment_like GROUP BY comment_id) AS l_count ON l_count.comment_id = c.id
        where c.id = #{commentId};
""")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "uid"),
            @Result(column = "parentId", property = "parent_id"),
            @Result(column = "content", property = "content"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "user_id", property = "user.userId"),
            @Result(column = "username", property = "user.username"),
            @Result(column = "like_count", property = "likes")
    })
    Comment queryComment(String commentId);

    @Select("select comment_id from comment_like " +
            "where exists(select id from comment where problem_id = #{problemId}) and user_id = #{userId} and is_like=true")
    List<Integer> queryCommentLikeList(@Param("problemId") String problemId,
                                       @Param("userId") String userId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into " +
            "comment (problem_id, user_id, parent_id, content) " +
            "values (#{problemId}, #{uid}, #{parentId}, #{content})")
    Integer insertComment(Comment comment);

    @Insert("""
        INSERT INTO comment_like (user_id, comment_id, is_like)
        VALUES (#{uid}, #{id}, 1)
        ON DUPLICATE KEY UPDATE
        is_like = (CASE WHEN is_like = 1 THEN 0 ELSE 1 END);
        """)
    Integer likeComment(Comment comment);
}
