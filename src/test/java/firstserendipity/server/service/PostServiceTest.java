//package firstserendipity.server.service;
//
//import firstserendipity.server.domain.dto.request.RequestPostDto;
//import firstserendipity.server.domain.dto.response.ResponsePostDto;
//import firstserendipity.server.domain.entity.Post;
//import firstserendipity.server.repository.PostRepository;
//import firstserendipity.server.util.mapper.PostMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@Transactional
////@SpringBootTest
//@Slf4j
//class PostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @Test
//    @DisplayName("게시글 등록 테스트")
//    void createdPostTest() {
//        //given
//        Post post = Post.builder()
//                .title("테스트제목")
//                .content("테스트내용")
//                .image("http://localhost8080~")
//                .build();
//        //when
//        Post savePost = postRepository.save(post);
//        //then
//        Assertions.assertThat(post).isEqualTo(savePost);
//        log.info(post.getTitle());
//        log.info(post.getContent());
//        log.info(post.getImage());
//    }
//}
//
//
