package firstserendipity.server.config;

import firstserendipity.server.domain.role.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class webSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationRequest -> {
                    authorizationRequest
                            .requestMatchers("/api/members/login", "/api/members/signup").permitAll() //로그인, 회원가입
                            .requestMatchers(GET, "/api/posts").permitAll()
                            .requestMatchers(GET, "/api/posts/{id}").permitAll()
                            .requestMatchers("/api/posts/today").permitAll()
                            .requestMatchers("/api/posts/good").permitAll()
                            .requestMatchers(POST, "/api/posts").hasRole("NAYOUNG") //게시글 작성
                            .requestMatchers(PUT, "/api/posts/{id}").hasRole("NAYOUNG") //게시글 수정
                            .requestMatchers(DELETE, "api/posts/{id}").hasRole("NAYOUNG") //게시글 삭제
                            .anyRequest().authenticated();
                })
                .build();
    }

}
