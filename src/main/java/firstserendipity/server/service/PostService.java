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

}
