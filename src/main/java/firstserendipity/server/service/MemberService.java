package firstserendipity.server.service;

import firstserendipity.server.domain.dto.request.RequestMemberSignupDto;
import firstserendipity.server.domain.dto.response.ResponseMemberSignupDto;
import firstserendipity.server.domain.entity.Member;
import firstserendipity.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static firstserendipity.server.util.mapper.MemberMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseMemberSignupDto signup(RequestMemberSignupDto requestDto) {
        String loginId = requestDto.getLoginId();
        String successMessage = "회원가입이 완료되었습니다.";

        if (validationDuplicateLoginId(loginId)) {
            throw new IllegalArgumentException("중복된 loginId 입니다.");
        }

        Member member = INSTANCE.requestMemberSignupDtoToEntity(requestDto);
        Member savedMember = memberRepository.save(member);

        return ResponseMemberSignupDto.builder()
                .id(savedMember.getId())
                .successMessage(successMessage)
                .build();
    }

    private boolean validationDuplicateLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }

}

