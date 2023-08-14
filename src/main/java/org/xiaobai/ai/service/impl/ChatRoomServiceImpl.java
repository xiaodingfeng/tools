package org.xiaobai.ai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaobai.ai.api.enums.ApiTypeEnum;
import org.xiaobai.ai.api.enums.ChatMessageTypeEnum;
import org.xiaobai.ai.entity.ChatMessageVO;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.entity.ChatRoomVO;
import org.xiaobai.ai.mapper.ChatRoomMapper;
import org.xiaobai.ai.request.ChatUserChatAddRequest;
import org.xiaobai.ai.request.query.ChatRoomPageQuery;
import org.xiaobai.ai.request.query.ChatRoomSearchRequest;
import org.xiaobai.ai.response.RoomInitResponse;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.ai.service.ChatModelInitWordsService;
import org.xiaobai.ai.service.ChatModelService;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.common.constants.SystemConstant;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.config.ChatConfig;
import org.xiaobai.core.enums.ExportTypeEnum;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.PageUtil;
import org.xiaobai.core.utils.WebUtil;
import org.xiaobai.tool.utils.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聊天室相关业务实现类
 */
@Service
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoomVO> implements ChatRoomService {

    @Lazy
    @Resource
    private ChatMessageService chatMessageService;
    @Resource
    private ChatModelService chatModelService;
    @Resource
    private ChatModelInitWordsService chatModelInitWordsService;
    @Resource
    private FileUtil fileUtil;
    @Resource
    private ChatConfig chatConfig;

    @Override
    public ChatRoomVO createChatRoom(ChatMessageVO chatMessageVO) {
        ChatRoomVO chatRoom = new ChatRoomVO();
        chatRoom.setId(IdWorker.getId());
        chatRoom.setApiType(chatMessageVO.getApiType());
        chatRoom.setIp(WebUtil.getIp());
        chatRoom.setFirstChatMessageId(chatMessageVO.getId());
        chatRoom.setFirstMessageId(chatMessageVO.getMessageId());
        // 取一部分内容当标题，可以通过 GPT 生成标题
        chatRoom.setTitle(StrUtil.sub(chatMessageVO.getContent(), 0, 50));
        chatRoom.setUserId(UserUtil.getUserId());
        chatRoom.setIsHide(false);
        save(chatRoom);
        return chatRoom;
    }

    @Override
    public Long createChatRoom(ChatUserChatAddRequest addRequest) {
        ChatModelVO serviceById = chatModelService.getById(addRequest.getModelId());
        if (StrUtil.isEmpty(addRequest.getTitle())) {
            addRequest.setTitle(serviceById.getTitle());
        }
        if (StrUtil.isEmpty(addRequest.getDescription())) {
            addRequest.setDescription(serviceById.getDescription());
        }
        ChatRoomVO chatRoom = new ChatRoomVO();
        chatRoom.setId(IdWorker.getId());
        chatRoom.setApiType(chatConfig.getApiTypeEnum());
        chatRoom.setIp(WebUtil.getIp());
        chatRoom.setFirstChatMessageId(null);
        chatRoom.setFirstMessageId(null);
        chatRoom.setTitle(addRequest.getTitle());
        chatRoom.setUserId(UserUtil.getUserId());
        chatRoom.setDescription(addRequest.getDescription());
        chatRoom.setModelId(addRequest.getModelId());
        chatRoom.setHisNum(addRequest.getHisNum());
        save(chatRoom);
        return chatRoom.getId();
    }

    @Override
    public List<ChatRoomVO> userList(ChatRoomSearchRequest searchRequest) {
        String keywords = searchRequest.getKeywords();
        boolean keywordsCondition = StrUtil.isNotEmpty(keywords);
        List<Long> roomIds = new ArrayList<>();
        if (keywordsCondition) {
            roomIds = chatMessageService.findRoomIdByKeyWords(keywords);
        }
        List<Long> finalRoomIds = roomIds;
        return list(new LambdaQueryWrapper<ChatRoomVO>()
                .eq(ChatRoomVO::getUserId, UserUtil.getUserId())
                .eq(ChatRoomVO::getIsHide, false)
                .and(keywordsCondition, v -> {
                     v.like(keywordsCondition, ChatRoomVO::getDescription, keywords)
                             .or()
                            .like(keywordsCondition, ChatRoomVO::getTitle, keywords)
                             .or()
                            .in(keywordsCondition, ChatRoomVO::getId, finalRoomIds);
                })
                .orderByDesc(ChatRoomVO::getId));
    }

    @Override
    public List<ChatMessageVO> listChat(Long roomId) {
        return chatMessageService.list(new LambdaQueryWrapper<ChatMessageVO>()
                .eq(ChatMessageVO::getChatRoomId, roomId)
                .orderByAsc(ChatMessageVO::getId));
    }

    @Override
    public IPage<ChatRoomVO> pageChatRoom(ChatRoomPageQuery chatRoomPageQuery) {
        Page<ChatRoomVO> chatRoomPage = page(new Page<>(chatRoomPageQuery.getPageNum()
                , chatRoomPageQuery.getPageSize()), new LambdaQueryWrapper<ChatRoomVO>()
                .and(StrUtil.isNotEmpty(chatRoomPageQuery.getKeywords()), v -> {
                    v.like(ChatRoomVO::getTitle, chatRoomPageQuery.getKeywords())
                            .or()
                            .like(ChatRoomVO::getDescription, chatRoomPageQuery.getKeywords())
                            .or()
                            .eq(BaseId::getId, chatRoomPageQuery.getKeywords())
                            .or()
                            .eq(ChatRoomVO::getModelId, chatRoomPageQuery.getKeywords());
                })
                .orderByDesc(ChatRoomVO::getId));

        return PageUtil.toPage(chatRoomPage, chatRoomPage.getRecords());
    }

    @Override
    public Boolean removeRoom(Long roomId) {
        return update(null, new LambdaUpdateWrapper<ChatRoomVO>()
                .eq(BaseId::getId, roomId)
                .set(ChatRoomVO::getIsHide, true));
    }

    @Override
    public RoomInitResponse initData(Long roomId) {
        RoomInitResponse response = new RoomInitResponse();
        ChatRoomVO roomVO = getById(roomId);
        if (roomVO == null) {
            throw new TipException("房间不存在");
        }
        response.setChatRoom(roomVO);
        response.setChatModel(chatModelService.getById(roomVO.getModelId()));
        response.setInitWords(chatModelInitWordsService.listByModel(roomVO.getModelId()));
        return response;
    }

    @Override
    public void exportHistoryMessage(HttpServletResponse response, Long roomId, String exportType) {
        ChatRoomVO chatRoomVO = this.getById(roomId);
        List<ChatMessageVO> messageList = this.listChat(roomId);
        if (CollectionUtils.isEmpty(messageList)) {
            throw new TipException("消息记录为空");
        }
        ExportTypeEnum exportTypeEnum = ExportTypeEnum.valueOf(exportType);
        switch (exportTypeEnum) {
            case MARK_DOWN: {
                exportMD(chatRoomVO, messageList, response);
            } break;
            case IMAGE: {
                exportIMG(chatRoomVO, messageList, response);
            } break;
            default: {
                throw new TipException("不支持的导出类型");
            }
        }
    }

    private void exportMD(ChatRoomVO chatRoomVO, List<ChatMessageVO> messageList, HttpServletResponse response) {
        List<String> stringList = messageList.stream().map(v -> {
            String who;
            if (v.getMessageType() == ChatMessageTypeEnum.QUESTION) {
                who = "**YOU**:  \n";
            } else {
                who = "**AI**:  \n";
            }
            return who + "\t\t" + v.getContent() + "  \n";
        }).collect(Collectors.toList());
        File markDownFile = fileUtil.createMarkDownFile(stringList, chatRoomVO.getTitle() + "-" + IdWorker.getMillisecond());
        fileUtil.down(markDownFile, response);
    }
    private void exportIMG(ChatRoomVO chatRoomVO, List<ChatMessageVO> messageList, HttpServletResponse response) {

    }

    @Override
    public Long createDefaultRoom() {
        if (getById(SystemConstant.GROUP_ROOM_ID) != null) {
            return SystemConstant.GROUP_ROOM_ID;
        }
        ChatRoomVO chatRoom = new ChatRoomVO();
        chatRoom.setId(SystemConstant.GROUP_ROOM_ID);
        chatRoom.setApiType(chatConfig.getApiTypeEnum());
        chatRoom.setFirstChatMessageId(null);
        chatRoom.setFirstMessageId(null);
        chatRoom.setTitle("公用附件");
        chatRoom.setUserId(Long.valueOf(SystemConstant.USER_CHAT_ID));
        chatRoom.setDescription("");
        chatRoom.setModelId(-1L);
        chatRoom.setHisNum(0);
        save(chatRoom);
        return chatRoom.getId();
    }
}
