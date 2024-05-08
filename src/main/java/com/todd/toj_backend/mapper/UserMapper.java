package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> queryAllUserList();

    @Select("select * from user where username=#{username}")
    User queryUserByUsername(@Param("username") String username);

    @Select("select * from user where user_id=#{userId}")
    User queryUserByUserId(@Param("userId") String userId);

    @Select("select * from user where nickname = #{nickname}")
    User queryUserByNickname(@Param("nickname") String nickname);

    @Insert("insert ignore into user (username, password, nickname) " +
            "VALUES (#{username}, #{password}, #{nickname})")
    Integer insertUser(User user);
}
