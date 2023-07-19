package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.service.CommentService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{id}")
    public ResponseEntity<ResponseResource> writeComment(
            @PathVariable Long id,
            @RequestBody @Valid RequestCommentDto requestCommentDto ,
            HttpServletRequest req
    ) {

        ResponseMessageDto responseDto = commentService.writeComment(id, requestCommentDto, req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responseDto))
                .build();

        WebMvcLinkBuilder linkBuilder = linkTo(CommentController.class);
        WebMvcLinkBuilder postControllerLinkBuilder = linkTo(PostController.class);

        responseResource.add(linkBuilder.slash("posts").slash(id).withSelfRel());
        responseResource.add(postControllerLinkBuilder.slash(id).withRel("getPosts"));

        return ResponseEntity.created(linkBuilder.slash("post").toUri()).body(responseResource);
    }

    @GetMapping
    public ResponseEntity<ResponseResource> getAllMyComments(HttpServletRequest req) {
        List<ResponseGetCommentDto> commentDtoList = commentService.getAllMyComments(req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(commentDtoList)
                .build();

        WebMvcLinkBuilder linkBuilder = linkTo(CommentController.class);
        responseResource.add(linkBuilder.withSelfRel());

        return ResponseEntity.ok(responseResource);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResource> deleteComment(@PathVariable Long id, HttpServletRequest req) {
        ResponseMessageDto responseDto = commentService.deleteComment(id, req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responseDto))
                .build();

        WebMvcLinkBuilder linkBuilder = linkTo(CommentController.class);
        responseResource.add(linkBuilder.slash("posts").slash(id).withSelfRel());
        responseResource.add(linkBuilder.withRel("getPosts"));

        return ResponseEntity.ok(responseResource);
    }
}
