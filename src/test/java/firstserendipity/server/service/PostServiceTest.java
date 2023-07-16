//package firstserendipity.server.service;
//
//import firstserendipity.server.domain.dto.request.RequestPostDto;
//import firstserendipity.server.domain.dto.response.ResponsePostDto;
//import firstserendipity.server.domain.entity.Post;
//import firstserendipity.server.repository.PostRespository;
//import firstserendipity.server.util.mapper.PostMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//@Transactional
//@SpringBootTest
//@Slf4j
//class PostServiceTest {
//
//    @Mock
//    private PostRespository postRepository;
//    private PostService postService;
//        @Test
//        @DisplayName("게시글 등록 테스트")
//        void createdPostTest(){
//            //given
//            Post post = Post.builder()
//                    .title("테스트제목")
//                    .content("테스트내용")
//                    .image("http://localhost8080~")
//                    .build();
//            //when
//            Post savePost = postRepository.save(post);
//            //then
//            Assertions.assertThat(post).isEqualTo(savePost);
//            log.info(post.getTitle());
//            log.info(post.getContent());
//            log.info(post.getImage());
//            log.info(post.getCreatedAt().toString());
//        }
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        postService = new PostService(postRepository);
//    }
//
//    @Test
//    @DisplayName("게시글 생성 테스트")
//    public void createPostTest() {
//        // Given
//        RequestPostDto requestDto = RequestPostDto.builder()
//                .title("테스트제목")
//                .content("테스트 내용")
//                .image("http://localhost8080~")
//                .build();
//
//        Post post = PostMapper.INSTANCE.toEntity(requestDto);
//
//        // When
//        ResponsePostDto responseDto = postService.createPost(null, requestDto);
//
//        // Then
//        assertEquals(post.getImage(), responseDto.getImage());
//        assertEquals(post.getTitle(), responseDto.getTitle());
//        assertEquals(post.getContent(), responseDto.getContent());
//
//
//        log.info(post.getTitle());
//        log.info(post.getContent());
//        log.info(post.getImage());
//
//    }
//    }
