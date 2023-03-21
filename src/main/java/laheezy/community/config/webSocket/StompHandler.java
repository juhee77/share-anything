package laheezy.community.config.webSocket;

import laheezy.community.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        System.out.println("message:" + message);
//        System.out.println("헤더 : " + message.getHeaders());
//        System.out.println("토큰" + accessor.getNativeHeader("Authorization"));
//        if(accessor.getCommand() == StompCommand.CONNECT) {
//            if(!tokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
//                throw new AccessDeniedException("");
//        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            tokenProvider.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));
        }
        return message;
    }
}