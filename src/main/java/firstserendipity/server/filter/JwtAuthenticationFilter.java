package firstserendipity.server.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstserendipity.server.domain.dto.request.RequestMemberLoginDto;
import firstserendipity.server.domain.role.Role;
import firstserendipity.server.security.jwt.JwtUserDetailsImpl;
import firstserendipity.server.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            RequestMemberLoginDto requestDto = objectMapper.readValue(request.getInputStream(), RequestMemberLoginDto.class);
            return authenticationRequest(requestDto);
        } catch (IOException e) {
            throw new RuntimeException("");
        }

    }

    private Authentication authenticationRequest(RequestMemberLoginDto requestDto) {
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getLoginId(),
                        requestDto.getPassword(),
                        null // TODO 리펙토링 가능한지 찾아보기
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = getJwtUserDetailsImpl(authResult).getUsername();
        Role role = getJwtUserDetailsImpl(authResult).getMember().getRole();

        String token = jwtUtil.createToken(username, role);

        jwtUtil.addJwtToHeader(token, response);
    }

    private static JwtUserDetailsImpl getJwtUserDetailsImpl(Authentication authResult) {
        return (JwtUserDetailsImpl) authResult.getPrincipal();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
