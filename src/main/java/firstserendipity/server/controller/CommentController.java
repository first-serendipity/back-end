package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.service.CommentService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}")
    public void writeComment(
            @PathVariable Long id,
            @RequestBody RequestCommentDto requestCommentDto,
            HttpServletRequest req
    ) {
        ResponseWriteCommentDto responseWriteCommentDto = commentService.writeComment(id, requestCommentDto, req);
        Map<String, Object> response = new HashMap<>();
        //ResponseEntity<Map<String, Object>>
//        List<Link> links = new ArrayList<>();
//        links.add(Link.of("http://localhost:8080/api/comments/post/" + id, "self"));
//        links.add(Link.of("http://localhost:8080/api/posts", "getPosts"));
//
//        ResponseResource<ResponseWriteCommentDto> responseResource = ResponseResource.
//                <ResponseWriteCommentDto>builder()
//                .responseDtos(Collections.singletonList(responseWriteCommentDto))
//                .build();
//
//        responseResource.add(links);
//          return responseResource;
    }

//    @GetMapping("/api/comments")
//    public void getAllMyComments(HttpServletRequest req){
//        commentService.getAllMyComments(req);
//    }
}
