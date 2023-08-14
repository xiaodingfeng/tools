package org.xiaobai.im.service.impl;

import io.netty.channel.ChannelId;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaobai.common.entity.SysUserVO;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.im.handler.ChannelHandlerPool;
import org.xiaobai.im.response.ImUserResponse;
import org.xiaobai.im.service.ImService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ImServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/17 14:22
 * @Version 1.0
 */
@Service
public class ImServiceImpl implements ImService {
    @Resource
    private SysUserService sysUserService;

    @Override
    public List<ImUserResponse> userList() {
        List<SysUserVO> userList = sysUserService.listValid();
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }
        Map<String, ChannelId> channelIdMap = ChannelHandlerPool.pool().getChannelIdMap();
        return userList.stream().map(v -> {
            ImUserResponse response = new ImUserResponse();
            response.setUserId(v.getId());
            response.setIsOnline(channelIdMap.containsKey(String.valueOf(v.getId())));
            response.setUserName(v.getUserName());
            response.setNickName(v.getNickName());
            return response;
        }).sorted(Comparator.comparing(ImUserResponse::getIsOnline).reversed()).collect(Collectors.toList());
    }
}
