package firstserendipity.server.controller;

import firstserendipity.server.domain.dto.request.RequestMemberSignupDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.service.MemberService;
import firstserendipity.server.util.resource.ResponseResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseResource> signup(@RequestBody @Valid RequestMemberSignupDto requestDto) {
        ResponseMessageDto responseDto = memberService.signup(requestDto);
        ArrayList<ResponseMessageDto> dtos = new ArrayList<>();
        dtos.add(responseDto);

        ResponseResource responseResource = ResponseResource.builder()
                .responseDtos(dtos)
                .build();

        WebMvcLinkBuilder linkBuilder = linkTo(MemberController.class);

        responseResource.add(linkBuilder.slash("signup").withSelfRel());
        responseResource.add(linkBuilder.slash("login").withRel("login"));

        return ResponseEntity.created(linkBuilder.slash("signup").toUri()).body(responseResource);
    }

}
