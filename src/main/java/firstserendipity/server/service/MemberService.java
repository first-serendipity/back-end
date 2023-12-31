package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestMemberSignupDto;
import firstserendipity.server.domain.dto.response.ResponseMessageDto;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static firstserendipity.server.util.mapper.MemberMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseMessageDto signup(RequestMemberSignupDto requestDto) {
        String successMessage = "회원가입이 완료되었습니다.";
        String loginId = requestDto.getLoginId();
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        if (validationDuplicateLoginId(loginId)) {
            throw new IllegalArgumentException("중복된 loginId 입니다.");
        }

        Member member = MEMBER_INSTANCE.requestMemberSignupDtoToEntity(requestDto);
        member.encodingPassword(encodePassword);
        memberRepository.save(member);

        return ResponseMessageDto.builder()
                .successMessage(successMessage)
                .build();
    }

    private boolean validationDuplicateLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }

}

