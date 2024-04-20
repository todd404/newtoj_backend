package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WhisperMapper {
    @Select("select * from whisper " +
            "where (send_user_id=#{userId} and receive_user_id=#{otherId}) " +
            "or (send_user_id=#{otherId} and receive_user_id=#{userId})")
    List<Whisper> queryWhispers(@Param("userId") String userId, @Param("otherId")String otherId);

    @Select("select count(*) as count, send_user_id from whisper where receive_user_id=#{userId} and is_read=0 group by send_user_id")
    List<UnreadWhisper> queryUnreadWhisper(String userId);

    @Insert("insert into whisper (send_user_id, receive_user_id, content) " +
            "VALUES (#{sendUserId}, #{receiveUserId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertWhisper(Whisper whisper);

    @Update("update whisper set is_read = true where send_user_id=#{sendUserId} and receive_user_id=#{receiveUserId}")
    Integer readAllWhisper(Whisper whisper);
}
