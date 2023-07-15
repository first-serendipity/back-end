package firstserendipity.server.service;

import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseCommentStatusMessageDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.role.Role;
import firstserendipity.server.repository.CommentRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static firstserendipity.server.util.mapper.CommentMapper.*;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public ResponseCommentStatusMessageDto writeComment(Long postId, RequestCommentDto requestCommentDto, HttpServletRequest req) {
        String successMsg = "댓글 작성이 완료되었습니다!";
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken = validateToken(req);
        //토큰으로 멤버 식별자 찾기
        Long memberId = findMemberIdFromToken(verifiedToken);
        //작성된 내용을 entity로 변환
        Comment comment = INSTANCE.commentDtoToCommentEntity(postId, memberId, requestCommentDto);
        //저장
        Comment saveComment = commentRepository.save(comment);
        //성공 msg Dto에 담아서 build
        return ResponseCommentStatusMessageDto.builder()
                .msg(successMsg)
                .build();

    }

    public List<ResponseGetCommentDto> getAllMyComments(HttpServletRequest req) {
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken = validateToken(req);
        //토큰에서 멤버 식별자 찾기
        Long memberId = findMemberIdFromToken(verifiedToken);
        //찾아올 댓글을 DTO로 바꿔서 담을 리스트 생성
        List<ResponseGetCommentDto> commentDtoList = new ArrayList<>();
        //멤버ID기준으로 댓글 찾아오기
        List<Comment> comments = commentRepository.findAllByMemberId(memberId);
        //찾아온 각 댓글을 dto로 변환해서 commentDtoList에 담기.
        for (Comment comment : comments) {
            commentDtoList.add(INSTANCE.commentEntityToGetDto(comment));
        }
        //DtoList로 반환
        return commentDtoList;

    }

    public ResponseCommentStatusMessageDto deleteComment(Long id, HttpServletRequest req) {
        String successMsg = "댓글 삭제가 완료되었습니다!";
        //토큰 검증 후 잘려진 토큰 반환
        String verifiedToken = validateToken(req);
        //권한 조회할 userInfo 생성
        Claims userInfo = getUserInfo(verifiedToken);
        //요청한 유저의 MemberId 조회
        Long requestedMemberId = findMemberIdFromToken(verifiedToken);
        // 댓글을 작성한 MemberId 조회
        Long writeMemberId = commentRepository.findMemberIdByCommentId(id);
        //삭제 요청자가 NAYOUNG 혹은 게시글을 작성한 유저가 아니라면 exception
        if (isNotWriteMemberOrNayoung(requestedMemberId, writeMemberId, userInfo)){
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        return ResponseCommentStatusMessageDto.builder()
                .msg(successMsg).build();
    }

    private boolean isNotWriteMemberOrNayoung(Long requestedMemberId, Long commentAuthorId, Claims userInfo) {
        if (requestedMemberId.equals(commentAuthorId)) return true;
        return userInfo.get("auth").equals(Role.NAYOUNG);
    }

    private String validateToken(HttpServletRequest req) {
        String userToken = req.getHeader("Authorization");
        //  jwt 토큰 substring
        String substringToken = jwtUtil.substringToken(userToken);
        // jwt 토큰 검증(필요한가..?)
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
        if (member.isEmpty()) throw new NullPointerException("해당 멤버를 찾을 수 없습니다.");
        //조회된 member의 식별자 반환
        return member.get().getId();
    }


    private Claims getUserInfo(String token) {
        // 사용자 정보 가져오기
        return jwtUtil.getUserInfoFromToken(token);
    }


}
