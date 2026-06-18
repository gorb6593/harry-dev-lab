package harry.backend.rab.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP over WebSocket 설정.
 *
 * - 클라이언트는 "/ws"로 연결한다. (SockJS 폴백 지원: 구형 환경/프록시 대응)
 * - 구독 주소 접두사 "/topic": 서버가 이 경로로 보낸 메시지를 구독자에게 브로드캐스트.
 * - 전송 주소 접두사 "/app": 클라이언트가 서버 @MessageMapping으로 보낼 때 사용.
 *
 * 지금은 내장(in-memory) 심플 브로커를 쓴다. 한 대 서버 기준 수천 동접까지는 충분하다.
 * 1만 명 이상 + 다중 서버로 확장할 때는 이 부분을 외부 브로커(Redis/RabbitMQ) 릴레이로 교체한다.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}
}
