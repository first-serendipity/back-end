package firstserendipity.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstserendipity.server.filter.JwtAuthenticationFilter;
import firstserendipity.server.filter.JwtAuthorizationFilter;
import firstserendipity.server.security.jwt.JwtUserDetailsServiceImpl;
import firstserendipity.server.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsServiceImpl jwtUserDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final WebConfig webConfig;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager 를 만들고 등록하는 코드
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, objectMapper);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setFilterProcessesUrl("/api/members/login");
        return filter;
    }
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtUtil, jwtUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(WebSecurityConfig::stateless)
                .authorizeHttpRequests(authorizationRequest -> {
                    authorizationRequest
                            .requestMatchers("/api/members/login", "/api/members/signup").permitAll() //로그인, 회원가입
                            .requestMatchers(GET, "/api/posts").permitAll()
                            .requestMatchers(GET, "/api/posts/{id}").permitAll()
                            .requestMatchers(GET,"/api/posts/today").permitAll()
                            .requestMatchers(GET,"/api/posts/good").permitAll()
                            .requestMatchers(GET,"/api/posts/goodlist").permitAll()
                            .requestMatchers(GET,"/api/posts/recent").permitAll()
                            .requestMatchers(POST, "/api/posts").hasRole("NAYOUNG") //게시글 작성
                            .requestMatchers(PUT, "/api/posts/{id}").hasRole("NAYOUNG") //게시글 수정
                            .requestMatchers(DELETE, "api/posts/{id}").hasRole("NAYOUNG") //게시글 삭제
                            .anyRequest().authenticated();
                })

                .addFilterBefore(webConfig.corsFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static void stateless(SessionManagementConfigurer<HttpSecurity> SessionManagementConfigurer) {
        SessionManagementConfigurer.sessionCreationPolicy(STATELESS);
    }

}
