package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.repository.CommentRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static firstserendipity.server.util.mapper.CommentMapper.*;
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public ResponseWriteCommentDto writeComment(Long postId, RequestCommentDto requestCommentDto, HttpServletRequest req) {
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken= validateToken(req);
        //토큰으로 memberId 찾기
        Long memberId = findMemberIdFromToken(verifiedToken);
        //작성된 내용을 entity로 변환
        Comment comment = INSTANCE.commentDtoToCommentEntity(postId, memberId, requestCommentDto);
        //저장
        Comment saveComment = commentRepository.save(comment);
        //저장된 엔티티를 responseDto로 변환해서 return
        return INSTANCE.commentEntityToCommentDto(saveComment);
    }
    private String validateToken(HttpServletRequest req) {
        String userToken = req.getHeader("Authorization");
        //  jwt 토큰 substring
        String substringToken= jwtUtil.substringToken(userToken);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(substringToken)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        //잘려진 토큰 다시 반환
        return substringToken;
    }
    private Long findMemberIdFromToken(String CompleteToken) {
        //토큰에서 claims 추출
        Claims userInfo = getUserInfo(CompleteToken);
        //추출된 loginID로 member 조회
        Optional<Member> member = memberRepository.findByLoginId(userInfo.getSubject());
        if(member.isEmpty()) throw new NullPointerException("해당 멤버를 찾을 수 없습니다.");
        //조회된 member의 식별자 반환
        return member.get().getId();
    }


    private Claims getUserInfo(String token){
        // 사용자 정보 가져오기
        return jwtUtil.getUserInfoFromToken(token);
    }


}
