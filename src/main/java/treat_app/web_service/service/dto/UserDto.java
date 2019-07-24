package treat_app.web_service.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {

    private Long id;
    private String userLogin;
    private String password;
    private List<TreatDto> treatDtos;
}
