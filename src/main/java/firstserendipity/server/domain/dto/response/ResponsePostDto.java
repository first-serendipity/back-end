package firstserendipity.server.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

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
    private LocalDateTime modifiedAt;
    private Boolean isLike;
    private List<ResponseGetCommentDto> comments;
}
