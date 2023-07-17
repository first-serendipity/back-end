package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.CommentRepository;
import firstserendipity.server.repository.LikeRepository;
import firstserendipity.server.repository.MemberRepository;
import firstserendipity.server.repository.PostRepository;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static firstserendipity.server.util.mapper.CommentMapper.*;
import static firstserendipity.server.util.mapper.PostMapper.POST_INSTANCE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeService likeService;

    //게시글 생성
    public ResponsePostDto createPost(RequestPostDto requestPostDto, HttpServletRequest req) {

        // token 가져오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);

        // 사용자 권한 가져오기
        String role = info.get("auth", String.class);

        if (!role.equals("NAYOUNG")) {
            throw new IllegalArgumentException("작성자만 등록할 수 있습니다.");
        }
        // RequestDto -> Entity
        Post post = POST_INSTANCE.RequestPostDtotoEntity(requestPostDto);
        // DB 저장
        Post savePost = postRepository.save(post);
        // Entity -> ResponseDto
        ResponsePostDto responsePostDto = POST_INSTANCE.PostEntitytoResponseDto(post);
        return responsePostDto;
    }

    // 전체 게시글 조회
    public List<ResponsePostDto> getPosts() {
        //builder 사용
//        return postRepository.findAllByOrderByCreatedAtDesc().stream()
//                .map(post -> ResponsePostDto.builder()
//                        .id(post.getId())
//                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .image(post.getImage())
//                        .build())
//                .toList();
        //Mapper 사용
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(POST_INSTANCE::PostEntitytoResponseDto)
                .collect(Collectors.toList());
    }

    // 2. 선택 게시글 조회 +) 선택한 게시글에 해당하는 댓글까지 모두 조회
    public ResponsePostDto getPost(Long id) {
        // 해당 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 댓글 조회
        List<Comment> comments = post.getComments();
        List<ResponseGetCommentDto> commentDtos = comments.stream()
                .map(COMMENT_INSTANCE::commentEntityToGetDto).toList();

        // Builder 사용
        return ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                .comments(commentDtos)
                .build();

        //Mapper 사용
//        return POST_INSTANCE.PostEntitytoResponseDto(post);
    }

    // 3. 랜덤을 기준으로 게시글 4개 조회
    public List<ResponsePostDto> getRandomPosts(){
        List<Post> allPosts = postRepository.findAll();
        // 셔플 해서 랜덤으로 돌려버리기
        Collections.shuffle(allPosts);
        //4개만 뽑아버리기
        List<Post> randomPosts = allPosts.stream().limit(4).collect(Collectors.toList());
        // postEntity -> ReponsePostDto 로 return 해주기
        return randomPosts.stream()
                .map(POST_INSTANCE::PostEntitytoResponseDto)
                .collect(Collectors.toList());
    }

    // 4. 좋아요를 기준으로 게시글 4개 조회
    public List<ResponsePostDto> getLikePosts(){
        List<Post> posts = postRepository.findAll();
        //좋아요 수를 기준으로 정렬
        posts.sort(Comparator.comparingLong(post -> likeService.getLikeCountByPostId(post.getId())));
        // 상위 4개만 게시글을 선택
        return posts.stream()
                .limit(4)
                .map(POST_INSTANCE::PostEntitytoResponseDto)
                .collect(Collectors.toList());
    }

    // 5. Member의 좋아요 리스트를 조회
    // 정렬 기준 - id
    public List<ResponsePostDto> getLikePosts(Long id, HttpServletRequest req){

        // 해당 member가 존재하는 확인
        Member member = findMember(id);
        // token 가져오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String memberId = info.getId();

        if(!member.getId().equals(memberId)){
            throw new IllegalArgumentException("해당 사용자가 아닙니다.");
        }

        //좋아요 리스트 조회
        List<Post> likePosts = likeRepository.findAllByMemberId(id);

        return likePosts.stream()
                .map(POST_INSTANCE::PostEntitytoResponseDto)
                .toList();
    }


    // 게시글 수정
    @Transactional
    public ResponsePostDto updatePost(Long id, RequestPostDto requestPostDto, HttpServletRequest req) {
        //해당 게시글의 존재 유무 확인
        Post post = findPost(id);
        // token 가져오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);;
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 권한 가져오기
        String role = info.get("auth", String.class);

        if (!role.equals("NAYOUNG")) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        // updatePost
//        Post postupdate = Post.builder()
//                .title(requestPostDto.getTitle())
//                .content(requestPostDto.getContent())
//                .image(requestPostDto.getImage())
//                .build();
        POST_INSTANCE.updateRequestPostDtotoEntity(requestPostDto,post);
        return POST_INSTANCE.PostEntitytoResponseDto(post);
    }


    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, HttpServletRequest req) {
        //해당 게시글의 존재 유무 확인
        Post post = findPost(id);
        // token 가져오기
        String tokenValue = req.getHeader("Authorization");
        //  jwt 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // jwt 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
        // 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 권한 가져오기
        String role = info.get("auth", String.class);

        if (!role.equals("NAYOUNG")) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        //게시글에 해당하는 댓글 삭제
        commentRepository.deleteAllByPostId(id);
        //게시글 삭제
        postRepository.delete(post);
    }


    // 게시글 존재유무 확인 메서드
    private Post findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return post;
    }

    private Member findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return member;
    }
}
