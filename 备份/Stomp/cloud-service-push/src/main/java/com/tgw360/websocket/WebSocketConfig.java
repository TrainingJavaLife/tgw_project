package com.tgw360.websocket;

import com.tgw360.management.SocketSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
// Spring配置，注入WebSocket，注入消息代理
// 关于心跳,客户端向一个指定的心跳路径发送心跳，服务器处理，
// 服务器使用指定的订阅路径向客户端发心跳。
// 重连客户端做

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	// 日志消息管理
	private static final Logger LOGGER = Logger.getLogger(WebSocketConfig.class.getName());
	// 添加服务端点，接收客户端的连接
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setHandshakeHandler(createDetermineHandler())
			.setAllowedOrigins("*").withSockJS();
	}
	// 配置消息代理
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 简单的内存消息代理，客户端订阅接收地址的前缀信息
		config.enableSimpleBroker("/topic");
		// 发送消息的前缀
		config.setApplicationDestinationPrefixes("/ws");
	}

	@Bean
	public StompDisconnectEvent webSocketDisconnectHandler() {
		return new StompDisconnectEvent();
	}
	@Bean
	public HandshakeHandler createDetermineHandler() {
		return new UserDetermineHandler();
	}

	// WebsocketSession注册容器
	@Bean
	public SocketSessionRegistry SocketSessionRegistry(){
		return new SocketSessionRegistry();
	}
	@Bean
	public STOMPConnectEvent STOMPConnectEventListener(){
		return new STOMPConnectEvent();
	}

	// 服务配置注册自定义认证拦截器, 这个拦截器需要认证并在连接信息中设置用户头
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(
				new ChannelInterceptorAdapter() {
					@Override
					public Message<?> preSend(Message<?> message, MessageChannel channel) {
						StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
						Map<String, Object> attr = accessor.getSessionAttributes();
						List tokenList = accessor.getNativeHeader("X-Authorization");
						if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
							LOGGER.log(Level.INFO, String.format("%s: %s", channel, message));
						}
						else if (StompCommand.CONNECT.equals(accessor.getCommand())) {
							LOGGER.log(Level.INFO, String.format("%s: %s", channel, message));
						}
						return message;
					}
				}
		);
	}
}
