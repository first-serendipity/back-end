package firstserendipity.server.controller;

import firstserendipity.server.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;

    // 리턴값 수정 필요
    @PostMapping("/likes/posts/{id}")
    public ResponseEntity<String> toggleLike(@PathVariable Long id, HttpServletRequest req){
     return likeService.toggleLike(id,req);
    }
}
