package org.xiaobai.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.xiaobai.ai.entity.ChatUserBalanceVO;

@Mapper
public interface ChatUserBalanceMapper extends BaseMapper<ChatUserBalanceVO> {
    @Update("update t_chat_user_balance set chat_balance = chat_balance - #{balance} where (chat_balance - #{balance}) > 0 and  user_id = #{userId}")
    int reduceChatBalance(@Param("userId") Long userId, @Param("balance") Integer balance);

    @Update("update t_chat_user_balance set chat_balance = chat_balance + #{balance} where user_id = #{userId}")
    int increaseChatBalance(@Param("userId") Long userId, @Param("balance") Integer balance);
}
