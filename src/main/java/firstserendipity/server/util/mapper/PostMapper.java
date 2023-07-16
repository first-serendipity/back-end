package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    @Mapping(target = "content", expression = "java(post.getContent() != null ? post.getContent() : \"\")")
    ResponsePostDto PostEntitytoResponseDto(Post post);
    @Mapping(target = "content", expression = "java(post.getContent() != null ? post.getContent() : \"\")")
    Post RequestPostDtotoEntity(RequestPostDto requestPostDto);
    @Mapping(target = "content", expression = "java(post.getContent() != null ? post.getContent() : \"\")")
    Post updateRequestPostDtotoEntity(RequestPostDto requestPostDto, @MappingTarget Post post);
}
