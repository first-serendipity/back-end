package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    ResponsePostDto PostEntitytoResponseDto(Post post);

//
//    default List<String> mapComments(List<Comment> comments) {
//        return comments.stream()
//                .map(Comment::getContent)
//                .collect(Collectors.toList());
//    }
//
//    @AfterMapping
//    default void addCommentsToDto(Post post, @MappingTarget ResponsePostDto.Builder responsePostDtoBuilder) {
//        responsePostDtoBuilder.comments(mapComments(post.getComments()));
//    }


    Post RequestPostDtotoEntity(RequestPostDto requestPostDto);

    Post updateRequestPostDtotoEntity(RequestPostDto requestPostDto, @MappingTarget Post post);
}
