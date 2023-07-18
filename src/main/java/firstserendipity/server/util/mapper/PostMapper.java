package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper POST_INSTANCE = Mappers.getMapper(PostMapper.class);

    ResponsePostDto postEntityToResponseDto(Post post);

    @Mapping(source = "postLikeCount", target = "likeCount")
    ResponsePostListDto postEntityToResponseDtoPostList(Post post, Integer postLikeCount);

    Post requestPostDtoToEntity(RequestPostDto requestPostDto, String image);
}
