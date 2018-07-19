package com.tgw360.websocket;


import com.tgw360.entity.websocket.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** WebSocket 握手拦截器做用户认证拦截处理 */
public class UserDetermineHandler extends DefaultHandshakeHandler {
    // 日志处理器
    private static final Logger LOGGER = Logger.getLogger(UserDetermineHandler.class.getName());
    // websocket握手处理
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        URI uri = request.getURI();
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        // 通过url的query参数获取认证参数
        String user = servletRequest.getServletRequest().getParameter("user");
        LOGGER.log(Level.INFO, "access_token:" + user);
        // 根据token认证用户，不通过返回拒绝握手
        Principal principal = authenticate(user);
        attributes.put("user", user);
        return principal;
    }
    private Principal  authenticate(String user) {
        //TODO 实现用户的认证并返回用户信息，如果认证失败返回 null
        return new StompPrincipal(user);
    }

}
