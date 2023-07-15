package firstserendipity.server.domain.dto.response;

import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseGetCommentDto {
    private final Long commentId;
    private final String nickname;
    private final String content;
    private final Long postId;
    private final Long memberId;

}
