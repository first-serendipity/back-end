package firstserendipity.server.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED) //new 생성자를 불가! .builder 로 만 가능하게!
@AllArgsConstructor(access = PROTECTED)
public class RequestPostDto {
    private String title;
    private String content;


}
