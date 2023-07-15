package firstserendipity.server.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestPostDto {
    private String title;
    private String content;
    private String image;


}
