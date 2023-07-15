package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.PostRespository;
import firstserendipity.server.util.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRespository postRespository;


    public ResponsePostDto createPost(RequestPostDto requestPostDto) {
        // RequestDto -> Entity
        Post post = PostMapper.INSTANCE.toEntity(requestPostDto);
        // DB 저장
        Post savePost = postRespository.save(post);
        // Entity -> ResponseDto
        ResponsePostDto responsePostDto =  PostMapper.INSTANCE.toResponseDto(post);

        return responsePostDto;
    }
}
