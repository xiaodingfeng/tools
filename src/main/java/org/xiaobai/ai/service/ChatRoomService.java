package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.request.ChatUserChatAddRequest;
import org.xiaobai.ai.request.query.ChatRoomPageQuery;
import org.xiaobai.ai.request.query.ChatRoomSearchRequest;
import org.xiaobai.ai.response.RoomInitResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 聊天室相关业务接口
 */
public interface ChatRoomService extends IService<ChatRoomVO> {

    /**
     * 创建聊天室
     *
     * @param chatMessageVO 聊天记录
     * @return 聊天室
     */
    ChatRoomVO createChatRoom(ChatMessageVO chatMessageVO);

    /**
     * 创建聊天室
     *
     * @param addRequest 聊天记录
     * @return 聊天室
     */
    Long createChatRoom(ChatUserChatAddRequest addRequest);

    /**
     * 用户会话列表
     *
     * @param searchRequest
     * @return List<ChatRoomVO>
     */
    List<ChatRoomVO> userList(ChatRoomSearchRequest searchRequest);

    /**
     * 会话id获取明细列表
     *
     * @param roomId
     * @return List<ChatUserChatListVO>
     */
    List<ChatMessageVO> listChat(Long roomId);

    /**
     * 聊天室分页
     *
     * @param chatRoomPageQuery 查询参数
     * @return 聊天室展示参数
     */
    IPage<ChatRoomVO> pageChatRoom(ChatRoomPageQuery chatRoomPageQuery);

    /**
     * 删除会话
     * @param roomId
     * @return
     */
    Boolean removeRoom(Long roomId);

    /**
     * 用户会话初始化接口
     * @param roomId
     * @return
     */
    RoomInitResponse initData(Long roomId);

    /**
     * 导出历史记录
     * @param response
     * @param roomId
     * @param exportType
     */
    void exportHistoryMessage(HttpServletResponse response, Long roomId, String exportType);

    /**
     * 创建一个默认房间
     * @return
     */
    Long createDefaultRoom();
}
