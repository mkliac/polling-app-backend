package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import voting.app.voting.app.dto.UserDto;
import voting.app.voting.app.model.User;
import voting.app.voting.app.model.UserWithDetailDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    @Mapping(source = "id", target = "email")
    UserDto toUserDto(User user);

    UserWithDetailDto toUserWithDetailDto(UserDto userDto);

    List<UserDto> toUserDtos(List<User> users);
}
