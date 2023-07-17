package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseCommentStatusMessageDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}")
    public ResponseEntity<Map<String, Object>> writeComment(
            @PathVariable Long id,
            @RequestBody RequestCommentDto requestCommentDto,
            HttpServletRequest req
    ) {
        ResponseCommentStatusMessageDto responseCommentStatusMessageDto = commentService.writeComment(id, requestCommentDto, req);
        // _links 정보 추가
        List<Link> links = new ArrayList<>();

        // self 링크 생성
        Link selfLink = WebMvcLinkBuilder.linkTo(CommentController.class).slash("post").slash(id).withSelfRel();
        links.add(selfLink);

        // getPosts 링크 생성. 근데 알아서 redirect로 가지는 url인데 여기 넣는게 맞는 지 확인 필요.
        Link getPostsLink = Link.of("http://localhost:8080/api/posts", "getPosts");
        links.add(getPostsLink);

        // 전체 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("_links", links);

        // data 정보 추가
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> dataItem = new HashMap<>();
        dataItem.put("msg", responseCommentStatusMessageDto.getMsg());
        data.add(dataItem);
        response.put("data", data);

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllMyComments(HttpServletRequest req){
        List<ResponseGetCommentDto> commentDtoList = commentService.getAllMyComments(req);

        // _links 정보 추가
        List<Link> links = new ArrayList<>();
        // self 링크 생성
        Link selfLink = WebMvcLinkBuilder.linkTo(CommentController.class).slash("").withSelfRel();
        links.add(selfLink);
        // deletePostLink . 전체댓글 조회라서 uri 뒤에 id는 어떻게 표시할지 모르겠음..
        Link deletePostsLink = Link.of("http://localhost:8080/api/posts", "deletePosts");
        links.add(deletePostsLink);
        // data 정보 추가
        Map<String, Object> response = new HashMap<>();
        response.put("_links", links);
        response.put("data", commentDtoList);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id, HttpServletRequest req){
        ResponseCommentStatusMessageDto responseCommentStatusMessageDto = commentService.deleteComment(id, req);

        List<Link> links = new ArrayList<>();
        // self 링크 생성
        Link selfLink = WebMvcLinkBuilder.linkTo(CommentController.class).slash(id).withSelfRel();
        links.add(selfLink);
        // getPosts 링크 생성
        Link getCommentsLink = WebMvcLinkBuilder.linkTo(CommentController.class).withRel("getComments");
        links.add(getCommentsLink);

        // 전체 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("_links", links);

        // data 정보 추가
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> dataItem = new HashMap<>();
        dataItem.put("msg", responseCommentStatusMessageDto.getMsg());
        data.add(dataItem);
        response.put("data", data);
        return ResponseEntity.ok().body(response);
    }
}
