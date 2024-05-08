package com.todd.toj_backend.mapper;

import com.todd.toj_backend.pojo.whisper.UnreadWhisper;
import com.todd.toj_backend.pojo.whisper.Whisper;
import com.todd.toj_backend.pojo.whisper.WhisperHistory;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface WhisperMapper {
    @Select("select * from whisper " +
            "where (send_user_id=#{userId} and receive_user_id=#{otherId}) " +
            "or (send_user_id=#{otherId} and receive_user_id=#{userId})")
    List<Whisper> queryWhispers(@Param("userId") String userId, @Param("otherId")String otherId);

    @Select("select count(*) as count, send_user_id from whisper where receive_user_id=#{userId} and is_read=0 group by send_user_id")
    List<UnreadWhisper> queryUnreadWhisper(String userId);

    @Select("SELECT send_user_id as user_id, sum(not is_read) as unreadCount, nickname, max(create_at) as last_message_date " +
            "FROM new_oj.whisper " +
            "left join user on user_id = send_user_id " +
            "where receive_user_id = #{userId} " +
            "group by send_user_id")
    List<WhisperHistory> queryReceiveWhisperHistory(String userId);

    @Select("SELECT receive_user_id as user_id, nickname, 0 as unreadCount, max(create_at) as last_message_date " +
            "FROM new_oj.whisper " +
            "left join user on user_id = receive_user_id " +
            "where send_user_id = #{userId} " +
            "group by receive_user_id")
    List<WhisperHistory> querySendWhisperHistory(String userId);

    @Select("select content from whisper " +
            "where send_user_id = #{sendUserId} and receive_user_id = #{receiveUserId} " +
            "ORDER BY id DESC LIMIT 1")
    String queryLastMessage(@Param("sendUserId") Integer sendUserId,
                            @Param("receiveUserId") Integer receiveUserId);

    @Insert("insert into whisper (send_user_id, receive_user_id, content) " +
            "VALUES (#{sendUserId}, #{receiveUserId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertWhisper(Whisper whisper);

    @Update("update whisper set is_read = true where send_user_id=#{sendUserId} and receive_user_id=#{receiveUserId}")
    Integer readAllWhisper(Whisper whisper);
}
