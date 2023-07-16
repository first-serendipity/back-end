package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    ResponsePostDto PostEntitytoResponseDto(Post post);
    Post RequestPostDtotoEntity(RequestPostDto requestPostDto);
    Post updateRequestPostDtotoEntity(RequestPostDto requestPostDto, @MappingTarget Post post);
}
