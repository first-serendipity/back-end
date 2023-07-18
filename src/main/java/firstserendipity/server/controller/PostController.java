package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
import firstserendipity.server.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

    // 추천 게시글 top 4 조회
    @GetMapping("/today")
    public ResponseEntity<ResponseResource> getRandomPosts() {
        List<ResponsePostListDto> responseDtoList = postService.getRandomPosts();
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responseDtoList)
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // 인기글 게시글 top 4 조회
    @GetMapping("/good")
    public ResponseEntity<ResponseResource> getLikePosts() {
        List<ResponsePostListDto> responseDtoList = postService.getLikePosts();
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responseDtoList)
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // member의 좋아요 리스트 조회
    @GetMapping("/likes")
    public ResponseEntity<ResponseResource> memberGetLikePosts(HttpServletRequest req){
        List<ResponsePostListDto> responseDtoList = postService.memberGetLikePosts(req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responseDtoList)
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // 최근 기록 내역 조회
    @GetMapping("/recent")
    public ResponseEntity<ResponseResource> getPostByIdRecent(@RequestBody List<Long> postIdList){
        List<ResponsePostListDto> responseDtoList = postService.getPostByIdRecent(postIdList);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responseDtoList)
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }
}
