package laheezy.community.config.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String HTTP_LOCALHOST_8080 = "*";
    @Autowired
    private StompHandler stompHandler; // jwt 인증

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();//추후에 도메인한정으로 수정해야한다.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");//추후에 도메인한정으로 수정해야한다.
        //registry.addEndpoint("/ws/stomp").setAllowedOrigins(HTTP_LOCALHOST_8080);//cors 방지를 위해서//추후에 도메인한정으로 수정해야한다.

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); //메세지 구독 (1:1, 1:다)
        registry.setApplicationDestinationPrefixes("/pub"); //메세지 발행
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}