package firstserendipity.server.domain.dto.response;

import firstserendipity.server.domain.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponsePostDto {
    private Long id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private List<ResponseGetCommentDto> comments;
}
