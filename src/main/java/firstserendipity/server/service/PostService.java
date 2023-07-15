package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.PostRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static firstserendipity.server.util.mapper.PostMapper.INSTANCE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;

    public ResponsePostDto createPost(RequestPostDto requestPostDto, HttpServletRequest req) {

        // token 가져오기
        String tokenValue = req.getHeader("Authorization");
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);

        // 사용자 권한 가져오기
        String role = info.get("auth", String.class);

        if (!role.equals("NAYOUNG")) {
            throw new IllegalArgumentException("작성자만 등록할 수 있습니다.");
        }
        // RequestDto -> Entity
        Post post = INSTANCE.RequestPostDtotoEntity(requestPostDto);
        // DB 저장
        Post savePost = postRepository.save(post);
        // Entity -> ResponseDto
        ResponsePostDto responsePostDto = INSTANCE.PostEntitytoResponseDto(post);
        return responsePostDto;
    }

    // 전체 게시글 조회
    public List<ResponsePostDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> ResponsePostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .image(post.getImage())
                        .build())
                .toList();
    }

    // 2. 선택 게시글 조회 +) 선택한 게시글에 해당하는 댓글까지 모두 조회
    public ResponsePostDto getPost(Long id) {

        // 해당 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 게시글 정보 & 댓글정보를 담을 DTO 생성
        ResponsePostDto responsePostDto = ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                // 해당 게시글에 해당하는 댓글 찾기
                .build();

        // 객체에 담아 return
        return responsePostDto;
    }

    // 3. 랜덤을 기준으로 게시글 조회
    public List<ResponsePostDto> getRandomPosts(){
        List<Post> allPosts = postRepository.findAll();
        // 셔플 해서 랜덤으로 돌려버리기
        Collections.shuffle(allPosts);
        //4개만 뽑아버리기
        List<Post> randomPosts = allPosts.stream().limit(4).collect(Collectors.toList());
        // postEntity -> ReponsePostDto 로 return 해주기
        return randomPosts.stream()
                .map(INSTANCE::PostEntitytoResponseDto)
                .collect(Collectors.toList());
    }

    // 4. 좋아요를 기준으로 게시글 조회

}
