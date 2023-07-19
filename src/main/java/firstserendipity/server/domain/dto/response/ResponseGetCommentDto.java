package firstserendipity.server.domain.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseGetCommentDto {
    private final Long commentId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
