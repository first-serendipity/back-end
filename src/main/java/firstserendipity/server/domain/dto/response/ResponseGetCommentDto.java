package firstserendipity.server.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseGetCommentDto {
    private final Long commentId;
    private final String nickname;
    private final String content;
    private final Long postId;
    private final Long memberId;

}
