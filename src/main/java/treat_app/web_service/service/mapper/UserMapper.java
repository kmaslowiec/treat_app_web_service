package treat_app.web_service.service.mapper;

import org.mapstruct.Mapper;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
