package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponseCommentStatusMessageDto;
import firstserendipity.server.domain.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

   CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
   ResponseCommentStatusMessageDto commentEntityToWriteDto(Comment comment);
   ResponseGetCommentDto commentEntityToGetDto(Comment comment);
   Comment commentDtoToCommentEntity(Long postId, Long memberId, RequestCommentDto requestCommentDto);
}
