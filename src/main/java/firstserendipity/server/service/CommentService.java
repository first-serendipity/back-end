package firstserendipity.server.service;

import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.CommentRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.repository.PostRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static firstserendipity.server.util.mapper.CommentMapper.*;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public ResponseMessageDto writeComment(Long id, RequestCommentDto requestCommentDto, HttpServletRequest req) {
        String successMessage = "댓글 작성이 완료되었습니다!";
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken = validateToken(req);
        //토큰으로 멤버 식별자 찾기
        Long memberId = findMemberIdFromToken(verifiedToken);
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong post id!!!"));
        //작성된 내용을 entity로 변환
        Comment comment = COMMENT_INSTANCE.commentDtoToCommentEntity(post, memberId, requestCommentDto);
        //저장
        commentRepository.save(comment);
        //성공 msg Dto에 담아서 build
        return COMMENT_INSTANCE.commentEntityToWriteDto(successMessage);
    }

    public List<ResponseGetCommentDto> getAllMyComments(HttpServletRequest req) {
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken = validateToken(req);

        Long memberId = findMemberIdFromToken(verifiedToken);

        List<ResponseGetCommentDto> commentDtoList = new ArrayList<>();

        List<Comment> comments = commentRepository.findAllByMemberId(memberId);

        for (Comment comment : comments) {
            commentDtoList.add(COMMENT_INSTANCE.commentEntityToGetDto(comment));
        }
        //DtoList로 반환
        return commentDtoList;

    }

    @Transactional
    public ResponseMessageDto deleteComment(Long id, HttpServletRequest req) {
        String successMessage = "댓글 삭제가 완료되었습니다!";
        String verifiedToken = validateToken(req);

        Claims userInfo = getUserInfo(verifiedToken);
        Long requestedMemberId = findMemberIdFromToken(verifiedToken);

        log.info("before validation");
        Comment comment = commentRepository.findByCommentId(id).orElseThrow(() -> new IllegalArgumentException("wrong comment id"));
        Long writeMemberId = comment.getMemberId();
        //삭제 요청자가 NAYOUNG 혹은 게시글을 작성한 유저가 아니라면 exception
        if (!isWriteMemberOrNayoung(requestedMemberId, writeMemberId, userInfo)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        log.info("after validation");
        log.info("requestedMemberId={}",requestedMemberId);
        log.info("writeMemberId={}",writeMemberId);
        commentRepository.deleteById(id);
        return COMMENT_INSTANCE.commentEntityToWriteDto(successMessage);
    }

    private boolean isWriteMemberOrNayoung(Long requestedMemberId, Long writeMemberId, Claims userInfo) {
        if (requestedMemberId.equals(writeMemberId)) {
            log.info("멤버id가 같음");
            return true;
        }

        String userRole = userInfo.get("auth", String.class);
        return userRole.equals("NAYOUNG");
    }

    private String validateToken(HttpServletRequest req) {
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        //  jwt 토큰 substring
        String substringToken = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(substringToken)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        //잘려진 토큰 다시 반환
        return substringToken;
    }

    private Long findMemberIdFromToken(String CompleteToken) {
        Claims userInfo = getUserInfo(CompleteToken);
        Optional<Member> member = memberRepository.findByLoginId(userInfo.getSubject());
        if (member.isEmpty()) throw new NullPointerException("해당 멤버를 찾을 수 없습니다.");
        return member.get().getId();
    }


    private Claims getUserInfo(String token) {
        return jwtUtil.getUserInfoFromToken(token);
    }


}
