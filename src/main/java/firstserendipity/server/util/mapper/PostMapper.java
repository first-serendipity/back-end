package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
import firstserendipity.server.domain.entity.Like;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PostMapper {
    PostMapper POST_INSTANCE = Mappers.getMapper(PostMapper.class);


    ResponsePostDto postEntityToResponseDto(Post post, Long postLikeCount, Boolean isLike);

    @Mapping(source = "likeCount", target = "likeCount")
    ResponsePostListDto postEntityToResponseDtoPostList(Post post, Long likeCount);

    ResponsePostDto postEntityToResponsePostDto(Post post, Boolean isLike, Long likeCount, List<ResponseGetCommentDto> comments);

    Post requestPostDtoToEntity(RequestPostDto requestPostDto, String image);

    default Post test(RequestPostDto requestPostDto, String image) {
        return Post.builder()
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .image(image)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }
}
