package org.xiaobai.ai.handler.emitter;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;

/**
 * ResponseBodyEmitter 链路
 * 责任链模式实现
 */
public interface ResponseEmitterChain {

    /**
     * 处理请求
     *
     * @param request 请求对象
     * @param emitter 响应对象
     */
    void doChain(ChatProcessRequest request, ResponseBodyEmitter emitter);

    /**
     * 获取下一个处理器
     *
     * @return 下一个处理器
     */
    ResponseEmitterChain getNext();

    /**
     * 设置下一个处理器
     *
     * @param next 下一个处理器
     */
    void setNext(ResponseEmitterChain next);

    /**
     * 获取前一个处理器
     *
     * @return 前一个处理器
     */
    ResponseEmitterChain getPrev();

    /**
     * 设置前一个处理器
     *
     * @param prev 前一个处理器
     */
    void setPrev(ResponseEmitterChain prev);
}
