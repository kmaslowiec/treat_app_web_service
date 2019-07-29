package treat_app.web_service.service.mapper;

import org.mapstruct.Mapper;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;

import java.util.List;


@Mapper(componentModel = "spring", uses = TreatMapper.class)
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto dto);

    List<TreatDto> toDtos(List<Treat> treats);

    List<Treat> toEntities(List<TreatDto> dtos);
}