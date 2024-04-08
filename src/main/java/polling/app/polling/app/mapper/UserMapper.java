package polling.app.polling.app.mapper;

import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import polling.app.polling.app.dto.user.UserDto;
import polling.app.polling.app.dto.user.UserWithDetailDto;
import polling.app.polling.app.model.user.User;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    @Mapping(source = "id", target = "email")
    UserDto toUserDto(User user);

    UserWithDetailDto toUserWithDetailDto(UserDto userDto);

    List<UserDto> toUserDtos(List<User> users);
}
