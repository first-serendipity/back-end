package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestPostDto;
import firstserendipity.server.domain.dto.response.ResponseGetCommentDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.domain.dto.response.ResponsePostDto;
import firstserendipity.server.domain.dto.response.ResponsePostListDto;
import firstserendipity.server.domain.entity.Comment;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.domain.entity.Post;
import firstserendipity.server.repository.*;
import firstserendipity.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static firstserendipity.server.util.mapper.CommentMapper.COMMENT_INSTANCE;
import static firstserendipity.server.util.mapper.PostMapper.POST_INSTANCE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final LikeRepository likeRepository;
    private final QueryRepository queryRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeService likeService;
    private final S3UploadService s3UploadService;
    private final Long FIND_MAX = 1L;
    private final Long RECENT_MAX = 9L;
    private final Long GOOD_MAX = 9L;


    //게시글 생성
    public ResponsePostDto createPost(RequestPostDto requestPostDto, MultipartFile image, HttpServletRequest req) throws IOException {

        String imageUrl =  s3UploadService.saveFile(image);
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
        System.out.println("role = " + role);
        log.info("role={}", info.get("auth", String.class));
        if (!role.equals("NAYOUNG")) {
            throw new IllegalArgumentException("작성자만 등록할 수 있습니다.");
        }

        // RequestDto -> Entity
        Post post = POST_INSTANCE.requestPostDtoToEntity(requestPostDto, imageUrl);
        // DB 저장
        Post savePost = postRepository.save(post);
        log.info("post={}", savePost.getCreatedAt());
        // Entity -> ResponseDto
        ResponsePostDto responsePostDto = POST_INSTANCE.postEntityToResponseDto(savePost);
        return responsePostDto;
    }

    // 1. 전체 게시글 조회
    public List<ResponsePostListDto> getPosts() {
        //Mapper 사용
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .collect(Collectors.toList());
    }

    // 1-2. 인기 게시글 조회 (9개)
    public List<ResponsePostListDto> getGoodPosts(){
        return queryRepository.findPostsByLikeCountDesc().stream()
                .limit(GOOD_MAX)
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .collect(Collectors.toList());
    }


    // 2. 선택 게시글 조회 +) 선택한 게시글에 해당하는 댓글까지 모두 조회
    public ResponsePostDto getPost(Long id, HttpServletRequest req) {
        // 해당 게시글 찾기
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 댓글 조회
        List<Comment> comments = post.getComments();
        List<ResponseGetCommentDto> commentDtos = comments.stream()
                .map(COMMENT_INSTANCE::commentEntityToGetDto).toList();


        //해당 유저 좋아요 여부
        // token 가져오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        Boolean isLike = false;
        if(isNotNullTokenValue(tokenValue)){
            //  jwt 토큰 substring
            String token = jwtUtil.substringToken(tokenValue);
            Claims info = jwtUtil.getUserInfoFromToken(token);
            String loginId=info.getSubject();
            Long memberId= memberRepository.findByLoginId(loginId).get().getId();
            isLike = likeRepository.existsByMemberIdAndPostId(memberId, id);
        }
        // Builder 사용
        return ResponsePostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .createdAt(post.getCreatedAt())
                .comments(commentDtos)
                .isLike(isLike)
                .build();
    }

    private static boolean isNotNullTokenValue(String tokenValue) {
        return tokenValue != null;
    }

    // 3. 랜덤을 기준으로 게시글 4개 조회
    public List<ResponsePostListDto> getRandomPosts() {
        List<Post> allPosts = postRepository.findAll();
        log.info("beforePost={}", allPosts);
        // 셔플 해서 랜덤으로 돌려버리기
        Collections.shuffle(allPosts);
        log.info("afterPost={}", allPosts);
        //4개만 뽑아버리기
        List<Post> randomPosts = allPosts.stream().limit(FIND_MAX).collect(Collectors.toList());
        // postEntity -> ReponsePostDto 로 return 해주기
        return randomPosts.stream()
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .collect(Collectors.toList());
    }

    // 4. 좋아요를 기준으로 게시글 4개 조회
    public List<ResponsePostListDto> getLikePosts() {
        List<Post> posts = postRepository.findAll();
        //좋아요 수를 기준으로 정렬
        posts.sort(Comparator.comparingLong(post -> likeService.getLikeCountByPostId(post.getId())));
        // 상위 4개만 게시글을 선택

        return posts.stream()
                .limit(FIND_MAX)
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .collect(Collectors.toList());
    }

    // 5. Member의 좋아요 리스트를 조회
    // 정렬 기준 - id
    public List<ResponsePostListDto> memberGetLikePosts(HttpServletRequest req) {

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
        String loginId = info.getSubject();

        // 해당 member가 존재하는 확인
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() ->
                new IllegalArgumentException("사용자가 존재하지 않습니다."));

        List<Post> likePosts = queryRepository.findPostsByMemberId(member.getId());

        return likePosts.stream()
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .toList();
    }


    // 게시글 수정
    @Transactional
    public ResponsePostDto updatePost(Long id, RequestPostDto requestPostDto, HttpServletRequest req) {
        String requestTitle = requestPostDto.getTitle();
        String requestContent = requestPostDto.getContent();

        //해당 게시글의 존재 유무 확인
        Post post = findPost(id);
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
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        post.updatePost(requestTitle, requestContent);

        return POST_INSTANCE.postEntityToResponseDto(post);
    }


    // 게시글 삭제
    @Transactional
    public ResponseMessageDto deletePost(Long id, HttpServletRequest req) {
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
        s3UploadService.deleteImage(post.getImage());
        //게시글에 해당하는 댓글 삭제, 좋아요 내역 삭제
        commentRepository.deleteAllByPostId(id);
        likeRepository.deleteAllByPostId(id);
        //게시글 삭제
        postRepository.delete(post);

        return ResponseMessageDto.builder()
                .successMessage("삭제가 완료되었습니다.")
                .build();
    }

    // 게시글 최근 내역 조회 메서드
    public List<ResponsePostListDto> getPostByIdRecent(List<Long> postIdList) {
        List<Post> recentList = postIdList.stream()
                .map(postId -> postRepository.findById(postId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return recentList.stream()
                .limit(RECENT_MAX)
                .map(post -> POST_INSTANCE.postEntityToResponseDtoPostList(post, post.getLikeCount()))
                .toList();
    }

    // 게시글 존재유무 확인 메서드
    private Post findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return post;
    }

}
