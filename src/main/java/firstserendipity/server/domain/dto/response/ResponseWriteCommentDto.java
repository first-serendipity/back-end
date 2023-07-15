package firstserendipity.server.domain.dto.response;

import lombok.Builder;

@Builder
public class ResponseWriteCommentDto {

    private final Long commentId;
    private final String msg;


}
