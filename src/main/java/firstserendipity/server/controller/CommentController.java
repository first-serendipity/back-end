package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.service.CommentService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}")
    public ResponseEntity<ResponseResource> writeCommentForPost(
            @PathVariable Long id,
            @RequestBody RequestCommentDto requestCommentDto,
            HttpServletRequest req
            ){
//        String a = req.getHeader("Authorization");
//        a.
//        ResponseWriteCommentDto responseWriteCommentDto = commentService.writeCommentForPost(id, requestCommentDto, req);
//        ResponseResource responseResource= ResponseResource.builder()
//                .build();
        return null;
    }
}
