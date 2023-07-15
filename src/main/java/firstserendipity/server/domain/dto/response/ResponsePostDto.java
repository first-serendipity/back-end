package firstserendipity.server.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponsePostDto {
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
}
