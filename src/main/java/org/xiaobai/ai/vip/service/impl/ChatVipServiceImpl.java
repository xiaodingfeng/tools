package org.xiaobai.ai.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.vip.entity.ChatVipVO;
import org.xiaobai.ai.vip.mapper.ChatVipMapper;
import org.xiaobai.ai.vip.response.UserVipInfoResponse;
import org.xiaobai.ai.vip.service.ChatVipPriceService;
import org.xiaobai.ai.vip.service.ChatVipService;
import org.xiaobai.ai.vip.service.ChatVipUserService;
import org.xiaobai.common.entity.BaseId;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.core.enums.ValidEnum;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ChatVipServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:07
 * @Version 1.0
 */
@Service
public class ChatVipServiceImpl extends ServiceImpl<ChatVipMapper, ChatVipVO> implements ChatVipService {

    @Resource
    private ChatVipPriceService chatVipPriceService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ChatVipUserService chatVipUserService;

    @Override
    public List<ChatVipVO> listValidVip() {
        return list(new LambdaQueryWrapper<ChatVipVO>()
        .eq(ChatVipVO::getValid,  ValidEnum.VALID)
        .orderByAsc(ChatVipVO::getSort)).stream()
                .peek(v -> v.setPrices(chatVipPriceService.listValidVipPrice(v.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ChatVipVO getValidVip(Long vip) {
         return getOne(new LambdaQueryWrapper<ChatVipVO>()
                .eq(ChatVipVO::getValid, ValidEnum.VALID)
                 .eq(BaseId::getId, vip));
    }

    @Override
    public UserVipInfoResponse userVipInfo(Long userId) {
        UserVipInfoResponse response = new UserVipInfoResponse();
        response.setUser(sysUserService.getById(userId));
        response.setVipInfo(chatVipUserService.getUserVip(userId));
        return response;
    }

    @Override
    public List<Map<String, Object>> vipTable() {
        List<ChatVipVO> validVip = this.listValidVip();
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> map;

        for (int i = 0; i < 2; i++) {
            map = new HashMap<>();
            for (ChatVipVO chatVipVO : validVip) {
                ChatVipVO.Options options = chatVipVO.getOptions();
                map.put(chatVipVO.getName(), options.getReceiveBalance());
            }
            result.add(map);
        }
        return result;
    }
}
