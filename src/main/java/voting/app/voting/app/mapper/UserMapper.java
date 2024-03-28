package voting.app.voting.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import voting.app.voting.app.dto.UserDto;
import voting.app.voting.app.model.User;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    @Mapping(source = "id", target = "email")
    UserDto toUserDto(User user);

    List<UserDto> toUserDtos(List<User> users);
}
