package firstserendipity.server.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstserendipity.server.domain.dto.request.RequestMemberLoginDto;
import firstserendipity.server.domain.dto.response.ResponseMemberDto;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.role.Role;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.security.jwt.JwtUserDetailsImpl;
import firstserendipity.server.service.MemberService;
import firstserendipity.server.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static firstserendipity.server.util.mapper.MemberMapper.*;
@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;



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
        Member member = memberRepository.findByLoginId(username).orElseThrow(()->new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        ResponseMemberDto responseDto = MEMBER_INSTANCE.MemberEntityToResponseMemberDto(member);
        Map<String, ResponseMemberDto> map = new HashMap<>();

        map.put("data", responseDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(map));

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
        response.getWriter().write("아이디 또는 비밀번호가 올바르지 않습니다");
    }
}
