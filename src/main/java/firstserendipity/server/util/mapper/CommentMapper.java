package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.domain.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
   ResponseWriteCommentDto commentEntityToCommentDto(Comment comment);
   Comment commentDtoToCommentEntity(Long postId, Long memberId, RequestCommentDto requestCommentDto);
}
