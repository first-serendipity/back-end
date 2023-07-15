package firstserendipity.server.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static lombok.AccessLevel.*;

@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class RequestMemberSignupDto {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])[a-z0-9]{5,10}$", message = "최소 5자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)만 가능합니다.")
    private String loginId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,15}$", message = "최소 8자 이상, 15자 이하이며 대소문자, 특수문자가 하나씩 포함되어야합니다.")
    private String password;

    @NotBlank
    private String nickname;

}
