package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    public User queryUserByUsername(@Param("username") String username);

    @Select("select * from user where user_id=#{userId}")
    User queryUserByUserId(@Param("userId") String userId);
}
