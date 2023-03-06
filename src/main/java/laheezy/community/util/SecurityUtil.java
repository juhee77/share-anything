package laheezy.community.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
public class SecurityUtil {

    public static Optional<String> getCurrentUserNickname()
    {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null){
            log.info("Secutiry context의 정보가 없음");
            return Optional.empty();
        }

        String userNickname = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userNickname = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userNickname = (String) authentication.getPrincipal();
        }
        return Optional.ofNullable(userNickname);
    }
}
