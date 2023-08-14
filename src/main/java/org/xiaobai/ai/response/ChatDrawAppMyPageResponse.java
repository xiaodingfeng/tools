package org.xiaobai.ai.response;

import lombok.Data;
import org.xiaobai.ai.entity.ChatDrawAppVO;
import org.xiaobai.ai.entity.ChatDrawPictureVO;

import java.util.List;

/**
 * @ClassName ChatDrawAppMyPageResponse
 * @Description 用户画图信息分页返回
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:14
 * @Version 1.0
 */
@Data
public class ChatDrawAppMyPageResponse extends ChatDrawAppVO {
    private List<ChatDrawPictureVO> pictureInfo;
}
