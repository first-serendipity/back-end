package firstserendipity.server.service;

import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.repository.CommentRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.util.mapper.CommentMapperImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommentMapperImpl commentMapperImpl;

    public ResponseWriteCommentDto writeCommentForPost(Long postId, RequestCommentDto requestCommentDto, HttpServletRequest req) {
        Long memberId=findMemberIdFromHeader(req);
        Comment comment = commentMapperImpl.commentDtoToCommentEntity(postId, memberId, requestCommentDto);
        Comment saveComment=commentRepository.save(comment);
        return commentMapperImpl.commentEntityToCommentDto(saveComment);
    }

    private Long findMemberIdFromHeader(HttpServletRequest req){
      String loginId=req.getHeader("Authorization");
        return memberRepository.findMemberIdByloginId(loginId);
   }
}
