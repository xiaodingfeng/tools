package org.xiaobai.ai.response;

import lombok.Data;
import org.xiaobai.ai.entity.ChatModelInitWordsVO;
import org.xiaobai.ai.entity.ChatModelVO;
import org.xiaobai.ai.entity.ChatRoomVO;

import java.util.List;

@Data
public class RoomInitResponse {
    private ChatRoomVO chatRoom;
    private ChatModelVO chatModel;
    private List<ChatModelInitWordsVO> initWords;
}
