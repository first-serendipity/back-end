package firstserendipity.server.service;

import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.domain.entity.Like;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.LikeRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.repository.PostRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public ResponseMessageDto toggleLike(Long id, HttpServletRequest req) {
        String likeMessage = "좋아요를 등록하였습니다.";
        String unlikeMessage = "좋아요를 취소하였습니다.";
        // token 가져오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findByLoginId(info.getSubject()).orElseThrow(() -> new IllegalArgumentException("wrong member!!!!!!!"));

        Long memberId = member.getId();
        // 좋아요 DB 조회 - memberId를 기준으로 postId를 조회
        Optional<Like> findLike = likeRepository.findByMemberIdAndPostId(memberId, id);

        // 있다면 삭제
        if (findLike.isPresent()) {
            likeRepository.deleteById(findLike.get().getId());
            return ResponseMessageDto.builder()
                    .successMessage(unlikeMessage)
                    .build();
        }
        Post post = postRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        // 없다면 등록
        Like like = Like.builder()
                .post(post)
                .memberId(memberId)
                .build();

        // 등록 후 저장
        likeRepository.save(like);

        return ResponseMessageDto.builder()
                .successMessage(likeMessage)
                .build();
    }

    // postId의 개수가 가장 많은 postId를 찾는다
    public Long getLikeCountByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
