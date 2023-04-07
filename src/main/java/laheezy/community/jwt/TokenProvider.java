package laheezy.community.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.exception.CustomSecurityException;
import laheezy.community.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;//30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //7일

    private final String secret;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }


    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(authentication.getName()) //payload
                .claim(AUTHORITIES_KEY, authorities) //payload
                .setExpiration(validity)//payload
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    //실제 로그인시 발급하는 토큰
    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = new Date().getTime();

        //access token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
//                .setHeader(header)
                .setSubject(authentication.getName()) //payload
                .claim(AUTHORITIES_KEY, authorities) //payload
                .setExpiration(accessTokenExpiresIn)//payload
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        //refresh token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한이 없는 토큰 입니다");
        }

        //권환이 있는 경우이다
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().
                    setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //토큰의 유효성검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; //문제 없음
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomSecurityException(ErrorCode.INVALID_JWT_AUTHORIZATION);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new CustomSecurityException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomSecurityException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomSecurityException(ErrorCode.INVALID_JWT_TOKEN);
        }
        //return false;
    }

    @Override

    public void afterPropertiesSet() throws Exception {
        byte[] decode = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decode);
    }
}
