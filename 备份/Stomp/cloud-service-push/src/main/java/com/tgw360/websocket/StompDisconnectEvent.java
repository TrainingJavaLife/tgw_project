package com.tgw360.websocket;

import com.tgw360.management.SocketSessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// STOMP是一个简单的面向文本的消息协议, 断开
@Component
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String  user = sha.getUser().getName();
        String sessionId = sha.getSessionId();
        webAgentSessionRegistry.unregisterSessionId(user, sessionId);

    }
}