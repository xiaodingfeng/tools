package org.xiaobai.im.handler;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.ssl.SslHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xiaobai.core.utils.TokenUtil;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.xiaobai.im.utils.AttributeKeyUtils.SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY;

@Slf4j
public class SecurityServerHandler extends ChannelInboundHandlerAdapter {

    private final String websocketPath;
    private final String subprotocols;
    private final boolean allowExtensions;
    private final int maxFramePayloadSize;
    private final boolean allowMaskMismatch;
    private final boolean checkStartsWith;

    /**
     * 自定义属性 token头key
     */
    private final String tokenHeader;
    /**
     * 自定义属性 token
     */
    private final boolean hasToken;


    public SecurityServerHandler(String websocketPath, String subprotocols,
                                 boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, String tokenHeader, boolean hasToken) {
        this(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch, false, tokenHeader, hasToken);
    }

    SecurityServerHandler(String websocketPath, String subprotocols,
                          boolean allowExtensions, int maxFrameSize,
                          boolean allowMaskMismatch,
                          boolean checkStartsWith,
                          String tokenHeader,
                          boolean hasToken) {
        this.websocketPath = websocketPath;
        this.subprotocols = subprotocols;
        this.allowExtensions = allowExtensions;
        maxFramePayloadSize = maxFrameSize;
        this.allowMaskMismatch = allowMaskMismatch;
        this.checkStartsWith = checkStartsWith;
        this.tokenHeader = tokenHeader;
        this.hasToken = hasToken;
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        String host = req.headers().get(HttpHeaderNames.HOST);
        return protocol + "://" + host + path;
    }

    private static void send100Continue(ChannelHandlerContext ctx, String tokenHeader, String token) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.CONTINUE);
        response.headers().set(tokenHeader, token);
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        final FullHttpRequest req = (FullHttpRequest) msg;
        if (isNotWebSocketPath(req)) {
            ctx.fireChannelRead(msg);
            return;
        }
        try {
            HttpHeaders headers = req.headers();
            String token = headers.get(tokenHeader);
            // 具体的鉴权逻辑
            if (hasToken) {
                if (StrUtil.isEmpty(token)) {
                    refuseChannel(ctx);
                    return;
                }
                // 开启鉴权 认证
                String userId = TokenUtil.getInstance().getUserId(token);
                if (StrUtil.isEmpty(userId)) {
                    refuseChannel(ctx);
                    return;
                }
                SecurityCheckComplete complete = new SecurityCheckComplete(userId, tokenHeader, hasToken, headers);
                ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).set(complete);
                ctx.fireUserEventTriggered(complete);
            } else {
                // 不开启鉴权 / 认证
                SecurityCheckComplete complete = new SecurityCheckComplete(null, tokenHeader, hasToken, headers);
                ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).set(complete);
            }
            if (req.method() != GET) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
                return;
            }
            final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(ctx.pipeline(), req, websocketPath), subprotocols,
                    allowExtensions, maxFramePayloadSize, allowExtensions);
            final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // 此处将具体的头加入http中，因为这个头会传递个netty底层设置响应头的方法中，默认实现是传的null
                HttpHeaders httpHeaders = new DefaultHttpHeaders().add(tokenHeader, token);
                // 此处便是构造握手相应头的关键步骤
                final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req, httpHeaders, ctx.channel().newPromise());
                handshakeFuture.addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        ctx.fireExceptionCaught(future.cause());
                    } else {
                        // Kept for compatibility
                        ctx.fireUserEventTriggered(
                                CustomWebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                        ctx.fireUserEventTriggered(
                                new CustomWebSocketServerProtocolHandler.HandshakeComplete(
                                        req.uri(), req.headers(), handshaker.selectedSubprotocol()));
                    }
                });
                CustomWebSocketServerProtocolHandler.setHandshaker(ctx.channel(), handshaker);
                ctx.pipeline().replace(this, "WS403Responder",
                        CustomWebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            req.release();
        }
    }

    private boolean isNotWebSocketPath(FullHttpRequest req) {
        return checkStartsWith ? !req.uri().startsWith(websocketPath) : !req.uri().equals(websocketPath);
    }

    private void refuseChannel(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("channel 捕获到异常了，关闭了");
        super.exceptionCaught(ctx, cause);
    }

    public static final class HandshakeComplete {
        private final String requestUri;
        private final HttpHeaders requestHeaders;
        private final String selectedSubprotocol;

        HandshakeComplete(String requestUri, HttpHeaders requestHeaders, String selectedSubprotocol) {
            this.requestUri = requestUri;
            this.requestHeaders = requestHeaders;
            this.selectedSubprotocol = selectedSubprotocol;
        }

        public String requestUri() {
            return requestUri;
        }

        public HttpHeaders requestHeaders() {
            return requestHeaders;
        }

        public String selectedSubprotocol() {
            return selectedSubprotocol;
        }
    }

    @Getter
    @AllArgsConstructor
    public static final class SecurityCheckComplete {

        private String userId;

        private String tokenHeader;

        private Boolean hasToken;

        private HttpHeaders requestHeaders;
    }
}
