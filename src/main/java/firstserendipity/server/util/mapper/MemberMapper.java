package firstserendipity.server.util.mapper;

import firstserendipity.server.domain.dto.request.RequestMemberSignupDto;
import firstserendipity.server.domain.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "role", constant = "MEMBER")
    Member requestMemberSignupDtoToEntity(RequestMemberSignupDto requestDto);

}
