package firstserendipity.server.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;

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
}
