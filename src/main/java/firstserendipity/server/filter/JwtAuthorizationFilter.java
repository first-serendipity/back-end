package firstserendipity.server.filter;

import firstserendipity.server.security.jwt.JwtUserDetailsServiceImpl;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsServiceImpl jwtUserDetailsService;
    @Autowired
    private RequestMappingInfoHandlerMapping handlerMapping;

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if(!isExistURI(request)){
//            System.out.println("존재하지 않는 url");
//            response.setStatus(HttpStatus.NOT_FOUND.value());
//            response.getWriter().write("요청 URL이 존재하지 않습니다.");
//            return;
//        }
        String tokenValue = jwtUtil.getTokenFromRequest(request); // good
        if (StringUtils.hasText(tokenValue)) {
            String token = jwtUtil.substringToken(tokenValue);
            if (!jwtUtil.validateToken(token)) {
                log.error("Token error");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("유효하지 않은 토큰입니다.");
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    //인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
    }

    protected boolean isExistURI(HttpServletRequest request) {
        HandlerExecutionChain handlerExecutionChain;
        try {
            handlerExecutionChain = handlerMapping.getHandler(request);
        } catch (Exception e) {
            handlerExecutionChain = null;
        }

        return handlerExecutionChain != null && handlerExecutionChain.getHandler() instanceof HandlerMethod;
    }
}
