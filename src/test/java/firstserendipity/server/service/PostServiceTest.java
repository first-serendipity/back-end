//package firstserendipity.server.service;
//
//import firstserendipity.server.domain.dto.request.RequestPostDto;
//import firstserendipity.server.domain.dto.response.ResponsePostDto;
//import firstserendipity.server.domain.entity.Post;
//import firstserendipity.server.repository.PostRepository;
//import firstserendipity.server.util.JwtUtil;
//import firstserendipity.server.util.mapper.PostMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.Collections;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@Slf4j
//@Transactional
//@SpringBootTest
//@EnableGlobalMethodSecurity(prePostEnabled=true)
//class PostServiceTest {
//
//    @Autowired
//    PostService postService;
//    @MockBean
//    MockHttpServletRequest mockHttpServletRequest;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @Test
//    @WithMockUser(username = "nayoung", roles = {"NAYOUNG"})
//    @DisplayName("게시글 등록 테스트")
//    void createdPostTest() throws Exception {
//        //given
////        String key = "testSecretKeytestSecretKeytestSecretKeytestSecretKey";
////        SecurityContext context = SecurityContextHolder.getContext();
////        Authentication authentication = context.getAuthentication();
////        authentication.setAuthenticated(true);
////
////        List<GrantedAuthority> authorities = Collections.singletonList(() -> "ROLE_NAYOUNG");
////        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
////        authenticationToken.setDetails(key);
////        context.setAuthentication(authenticationToken);
//
//        RequestPostDto requestPostDto = RequestPostDto.builder()
//                .title("테스트제목")
//                .content("테스트내용")
//                .build();
//
//        FileInputStream fileInputStream = new FileInputStream("C:/SpartaCodingProjects/back-end/file.png");
//        MultipartFile multipartFile = new MockMultipartFile("file", "file.png", "image/png", fileInputStream);
//
//        //when
//        ResponsePostDto responsePostDto = postService.createPost(requestPostDto, multipartFile, mockHttpServletRequest);
//        //then
//        assertThat(responsePostDto.getTitle()).isEqualTo(requestPostDto.getTitle());
//        assertThat(responsePostDto.getContent()).isEqualTo(requestPostDto.getContent());
//    }
//}


