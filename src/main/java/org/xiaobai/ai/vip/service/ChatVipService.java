package org.xiaobai.ai.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.vip.entity.ChatVipVO;
import org.xiaobai.ai.vip.response.UserVipInfoResponse;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ChatVipService
 * @Description 会员信息
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:06
 * @Version 1.0
 */
public interface ChatVipService extends IService<ChatVipVO> {
    /**
     * 会员列表
     * @return
     */
    List<ChatVipVO> listValidVip();

    /**
     * 获取正在生效vip信息
     * @param vip
     */
    ChatVipVO getValidVip(Long vip);

    /**
     * 用户会员信息
     *
     * @return
     */
    UserVipInfoResponse userVipInfo(Long userId);

    /**
     * 会员对比表格
     *
     * @return
     */
    List<Map<String, Object>> vipTable();
}
