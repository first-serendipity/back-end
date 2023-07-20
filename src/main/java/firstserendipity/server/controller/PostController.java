package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
import firstserendipity.server.service.PostService;
import firstserendipity.server.service.S3UploadService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final S3UploadService s3UploadService;
    // 관리자 권한의 게시글 작성 ->
    @PostMapping
    public ResponseEntity<ResponseResource> createPost(
            @RequestPart(value = "image",required = false) MultipartFile image,
            @RequestPart("data") RequestPostDto requestPostDto,
            HttpServletRequest req) throws IOException {
        ResponsePostDto responsePostDto = postService.createPost(requestPostDto,image,req);
        // ResponseResource 생성
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responsePostDto))
                .build();

        WebMvcLinkBuilder postLinkBuilder = linkTo(PostController.class); //postController에 대한 링크생성
        //생성된 포스트의 self-link 추가
        responseResource.add(postLinkBuilder.withSelfRel());
        //전체 포스트 조회 link 추가
        responseResource.add(postLinkBuilder.withRel("getPosts"));
        return ResponseEntity.created(postLinkBuilder.toUri()).body(responseResource);
    }

    // 나영님의 게시글 수정
    //TODO 게시글 수정 시 사진도 수정 가능하게끔
    @PutMapping("/{id}")
    public ResponseEntity<ResponseResource> updatePost(
            @PathVariable Long id,
            @RequestPart("data") RequestPostDto requestPostDto,
            HttpServletRequest req) {

        ResponsePostDto responsePostDto = postService.updatePost(id, requestPostDto, req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responsePostDto))
                .build();

        WebMvcLinkBuilder updatePostLinkBuilder = linkTo(PostController.class);
        responseResource.add(updatePostLinkBuilder.withSelfRel());
        responseResource.add(updatePostLinkBuilder.withRel("updatePost"));

        return ResponseEntity.ok(responseResource);
    }

    // 나영님의 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResource> deletePost(@PathVariable Long id, HttpServletRequest req) {
        ResponseMessageDto responseMessageDto = postService.deletePost(id,req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responseMessageDto))
                .build();

        WebMvcLinkBuilder deletePostLinkBuilder = linkTo(PostController.class);
        responseResource.add(deletePostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // 전체 게시글 조회
    @GetMapping
    public ResponseEntity<ResponseResource> getPosts() {
        List<ResponsePostListDto> responsePostDto = postService.getPosts();
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responsePostDto)
                .build();

        WebMvcLinkBuilder getPostLinkBuilder = linkTo(PostController.class);
        responseResource.add(getPostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // 인기 게시글 조회
    @GetMapping("/goodlist")
    public ResponseEntity<ResponseResource> getGoodPosts(){
        List<ResponsePostListDto> responsePostDto = postService.getGoodPosts();
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responsePostDto)
                .build();

        WebMvcLinkBuilder getPostLinkBuilder = linkTo(PostController.class);
        responseResource.add(getPostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    //선택 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseResource> getPost(@PathVariable Long id, HttpServletRequest req){
        ResponsePostDto responsePostDto = postService.getPost(id, req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responsePostDto))
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());
        responseResource.add(PostLinkBuilder.slash(id).withRel("updatePost"));
        responseResource.add(PostLinkBuilder.slash(id).withRel("deletePost"));

        return ResponseEntity.ok(responseResource);
    }

    // 추천 게시글 top 4 조회
    @GetMapping("/today")
    public ResponseEntity<ResponseResource> getRandomPosts(){
        List<ResponsePostListDto> responseDtoList = postService.getRandomPosts();
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(responseDtoList)
                .build();

        WebMvcLinkBuilder PostLinkBuilder = linkTo(PostController.class);
        responseResource.add(PostLinkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }

    // 인기글 게시글 top 1 조회
    @GetMapping("/good")
    public ResponseEntity<ResponseResource> getBestPost(){
        List<ResponsePostListDto> responseDtoList = postService.getBestPost();
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
