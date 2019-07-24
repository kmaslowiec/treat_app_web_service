package treat_app.web_service.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String userLogin;
    private String password;
    private List<TreatDto> treatDtos;
}
