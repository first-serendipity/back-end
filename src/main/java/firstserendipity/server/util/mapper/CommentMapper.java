package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponseCommentStatusMessageDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

   CommentMapper COMMENT_INSTANCE = Mappers.getMapper(CommentMapper.class);

   ResponseCommentStatusMessageDto commentEntityToWriteDto(Comment comment);
   ResponseGetCommentDto commentEntityToGetDto(Comment comment);
   @Mapping(source = "requestCommentDto.content", target = "content")
   Comment commentDtoToCommentEntity(Post post, Long memberId, RequestCommentDto requestCommentDto);
}
