package firstserendipity.server.domain.dto.response;

import firstserendipity.server.domain.role.Role;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class ResponseMemberDto {

    @NotBlank
    private String loginId;

    @Enumerated(STRING)
    private Role role;

    @NotBlank
    private String nickname;

}
