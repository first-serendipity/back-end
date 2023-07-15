package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 나영님의 게시글 작성
    @PostMapping("/posts")
    public ResponsePostDto createPost(@RequestBody RequestPostDto requestPostDto, HttpServletRequest req) {
        return postService.createPost(requestPostDto,req);
    }
    // 전체 게시글 조회
    @GetMapping("/posts")
    public List<ResponsePostDto> getPosts(){
        return postService.getPosts();
    }
    // 인기글 게시글 조회
    // 추천 게시글 조회

}
