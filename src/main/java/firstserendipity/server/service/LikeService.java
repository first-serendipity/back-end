package firstserendipity.server.service;

import firstserendipity.server.domain.entity.Like;
import firstserendipity.server.repository.LikeRepository;
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
    private final JwtUtil jwtUtil;
    public ResponseEntity<String> toggleLike(Long postId, HttpServletRequest req) {
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
        Long memberId = info.get("id", Long.class);


        // 좋아요 DB 조회 - postID, memberId 두개를 기준으로 조회
        Optional<Like> checklike = likeRepository.existsByMemberIdAndPostId(memberId,postId);
        // 있다면 삭제
        if(checklike.isPresent()){
            likeRepository.deleteById(checklike.get().getId());
            return ResponseEntity.ok("좋아요를 삭제하였습니다.");
        }
        // 없다면 등록
        Like like = Like.builder()
                .postId(postId)
                .memberId(memberId)
                .build();

        // 등록 후 저장
        likeRepository.save(like);

        return ResponseEntity.ok("좋아요를 등록하였습니다.");
    }

    // postId의 개수가 가장 많은 postId를 찾는다
    public Long getLikeCountByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
