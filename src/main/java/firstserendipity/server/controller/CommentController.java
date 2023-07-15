package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseWriteCommentDto;
import firstserendipity.server.service.CommentService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
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
      //반환 형태  ResponseEntity<Map<String, Object>>

        // _links 정보 추가
//        List<Map<String, String>> links = new ArrayList<>();
//        Map<String, String> link1 = new HashMap<>();
//        link1.put("rel", "self");
//        link1.put("href", "http://localhost:8080/api/example");
//        Map<String, String> link2 = new HashMap<>();
//        link2.put("rel", "getPosts");
//        link2.put("href", "http://localhost:8080/api/posts");
//        links.add(link1);
//        links.add(link2);
//        response.put("_links", links);
//
//        // data 정보 추가 (배열 형태)
//        List<Map<String, String>> data = new ArrayList<>();
//        Map<String, String> dataItem = new HashMap<>();
//        dataItem.put("msg", "댓글 작성이 완료되었습니다.");
//        data.add(dataItem);
//        response.put("data", data);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/comments")
    public void getAllMyComments(HttpServletRequest req){
        commentService.getAllMyComments(req);
    }
}
