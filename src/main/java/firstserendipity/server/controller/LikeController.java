package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.service.LikeService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/likes/posts/{id}")
    public ResponseEntity<ResponseResource> toggleLike(@PathVariable Long id, HttpServletRequest req){
        ResponseMessageDto responseDto = likeService.toggleLike(id,req);
        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(Collections.singletonList(responseDto))
                .build();

        WebMvcLinkBuilder linkBuilder = linkTo(LikeController.class);
        responseResource.add(linkBuilder.slash("likes").slash("posts").slash(id).withSelfRel());
        return ResponseEntity.ok(responseResource);
    }
}
