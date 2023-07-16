package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestCommentDto;
import firstserendipity.server.domain.dto.response.ResponseCommentStatusMessageDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
        String selfUri=req.getRequestURL().toString();

        Map<String, Object> response = new HashMap<>();
        // _links 정보 추가
        List<Map<String, String>> links = new ArrayList<>();
        Map<String, String> link1 = new HashMap<>();
        link1.put("rel", "self");
        link1.put("href", selfUri);
        Map<String, String> link2 = new HashMap<>();
        link2.put("rel", "getPosts");
        link2.put("href", "http://localhost:8080/api/posts");
        links.add(link1);
        links.add(link2);
       response.put("_links", links);

        // data 정보 추가 (배열 형태)
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> dataItem = new HashMap<>();
        dataItem.put("msg", responseCommentStatusMessageDto.getMsg());
        data.add(dataItem);
       response.put("data", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllMyComments(HttpServletRequest req){
        List<ResponseGetCommentDto> commentDtoList = commentService.getAllMyComments(req);
        String selfUri=req.getRequestURL().toString();

        Map<String, Object> response = new HashMap<>();
        // _links 정보 추가
        List<Map<String, String>> links = new ArrayList<>();
        Map<String, String> link1 = new HashMap<>();
        link1.put("rel", "self");
        link1.put("href", selfUri);
        Map<String, String> link2 = new HashMap<>();
        link2.put("rel", "getPosts");
        link2.put("href", "http://localhost:8080/api/posts");
        links.add(link1);
        links.add(link2);
        response.put("_links", links);
        response.put("data", commentDtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, HttpServletRequest req){
        commentService.deleteComment(id, req);
    }
}
