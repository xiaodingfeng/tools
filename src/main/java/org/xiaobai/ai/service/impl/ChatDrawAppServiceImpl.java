package org.xiaobai.ai.service.impl;

import cn.hutool.core.img.ImgUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.entity.ChatDrawAppVO;
import org.xiaobai.ai.mapper.ChatDrawAppMapper;
import org.xiaobai.ai.request.ChatDrawAppAddTaskRequest;
import org.xiaobai.ai.request.ChatDrawAppMyPageRequest;
import org.xiaobai.ai.response.ChatDrawAppMyPageResponse;
import org.xiaobai.ai.service.ChatDrawAppService;
import org.xiaobai.core.utils.ObjectMapperUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.IMAGE_PNG;

/**
 * @ClassName ChatDrawAppServiceImpl
 * @Description 用户画图信息
 * @Author dingfeng.xiao
 * @Date 2023/6/30 16:10
 * @Version 1.0
 */
@Slf4j
@Service
public class ChatDrawAppServiceImpl extends ServiceImpl<ChatDrawAppMapper, ChatDrawAppVO> implements ChatDrawAppService {
    @Override
    public Page<ChatDrawAppMyPageResponse> myListPage(Long userId, ChatDrawAppMyPageRequest pageRequest) {
        Page<ChatDrawAppVO> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<ChatDrawAppVO> data = page(page, new LambdaQueryWrapper<ChatDrawAppVO>()
                .eq(ChatDrawAppVO::getUserId, userId));
        Page<ChatDrawAppMyPageResponse> result = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        if (data.getRecords().size() > 0) {
            BeanUtils.copyProperties(data, result);
            result.setRecords(data.getRecords().stream().map(record -> {
                ChatDrawAppMyPageResponse response = new ChatDrawAppMyPageResponse();
                BeanUtils.copyProperties(record, response);
                // TODO 补充图片信息
                response.setPictureInfo(Collections.emptyList());
                return response;
            }).collect(Collectors.toList()));
            return result;
        }
        return result;
    }

    @Override
    public ResponseBodyEmitter addTask(Long userId, ChatDrawAppAddTaskRequest drawAppAddTaskRequest) throws IOException {
        ChatDrawAppVO chatDrawAppVO = new ChatDrawAppVO();
        chatDrawAppVO.setConfig(drawAppAddTaskRequest.getConfig());
        chatDrawAppVO.setMode(drawAppAddTaskRequest.getMode());
        chatDrawAppVO.setPrompt(drawAppAddTaskRequest.getPrompt());
        chatDrawAppVO.setStatus(0);
        chatDrawAppVO.setUserId(userId);
        save(chatDrawAppVO);
        // 发送请求图片生成
        return getImage(chatDrawAppVO);
    }

    private ResponseBodyEmitter getImage(ChatDrawAppVO chatDrawAppVO) throws IOException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        emitter.onCompletion(() -> log.info("请求参数：{}，Front-end closed the emitter connection.", ObjectMapperUtil.toJson(chatDrawAppVO)));
        emitter.onTimeout(() -> log.error("请求参数：{}，Back-end closed the emitter connection.", ObjectMapperUtil.toJson(chatDrawAppVO)));


        try {
            BufferedImage read = ImgUtil.read(new URL("https://chat.fengzhengx.cn/static/img/ani.51cdc48d.jpeg"));
            emitter.send(read, IMAGE_PNG);
        } finally {
            emitter.complete();
        }
        return emitter;
    }
}
