package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
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
    // 나영님의 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponsePostDto updatePost(@PathVariable Long id, @RequestBody RequestPostDto requestPostDto, HttpServletRequest req) {
        return postService.updatePost(id,requestPostDto,req);
    }
    // 나영님의 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest req) {
        postService.deletePost(id,req);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

    // 전체 게시글 조회
    @GetMapping("/posts")
    public List<ResponsePostDto> getPosts(){return postService.getPosts();}

    //선택 게시글 조회
    @GetMapping("/posts/{id}")
    public ResponsePostDto getPost(@PathVariable Long id, HttpServletRequest req){ return postService.getPost(id, req);}

    // 추천 게시글 조회
    @GetMapping("/posts/today")
    public List<ResponsePostListDto> getRandomPosts(){
        return postService.getRandomPosts();
    }

    // 인기글 게시글 조회
    @GetMapping("/posts/good")
    public List<ResponsePostListDto> getLikePosts(){
        return postService.getLikePosts();
    }

    // member의 좋아요 리스트 조회
    @GetMapping("/posts/likes")
    public List<ResponsePostListDto> memberGetLikePosts(HttpServletRequest req){
        return postService.memberGetLikePosts(req);
    }

    // 최근 기록 내역 조회
    @GetMapping("/post/recent")
    public List<ResponsePostListDto> getPostByIdRecent(@RequestBody List<Long> postIdList){
        return postService.getPostByIdRecent(postIdList);
    }
}
