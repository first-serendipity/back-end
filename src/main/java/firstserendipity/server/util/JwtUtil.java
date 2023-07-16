package firstserendipity.server.util;

import firstserendipity.server.domain.role.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Date;
@Component
@Slf4j
public class JwtUtil {
    private final String HEADER_NAME = "Authorization";
    private final String BEARER = "Bearer ";
    private final String AUTHORIZATION_KEY = "auth";
    private final Long EXPIRATION_TIME = 60 * 60 * 1000L;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String loginId, Role role) {
        Date date = new Date();

        return BEARER + Jwts.builder()
                .setSubject(loginId)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + EXPIRATION_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }
    // 2. 생성된 JWT를 Response 객체 Header에 바로 넣어버리기!

//    public void addJwtToHeader(String token, HttpServletRequest req){
//        req.addHeader(HEADER_NAME, token);
//    }

    // 3. Header에 들어있는 JWT 토큰을 Substring

    public String substringToken(String tokenValue){
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER)){
            //공백, null을 확인 할 수 있음 && BEARER_PREFIX로 시작하는지 알 수 있음.
            return tokenValue.substring(7);
        }
        log.error("NOT FOUND TOKEN");
        throw new NullPointerException("NOT FOUND TOKEN");
    }

    //    4. JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // key로 token 검증
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //    5. JWT에서 사용자 정보 가져오기

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); //body부분의 claims를 가지고 올 수 잇음
    }
}