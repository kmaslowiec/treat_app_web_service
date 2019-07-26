package treat_app.web_service.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.service.dto.TreatDto;

@Mapper(componentModel = "spring")
public interface TreatMapper {

    @Mapping(source = "user.id", target = "userId")
    TreatDto toDto(Treat treat);

    @Mapping(source = "userId", target = "user.id")
    Treat toEntity(TreatDto treatDto);
}