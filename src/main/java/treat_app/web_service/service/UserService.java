package treat_app.web_service.service;

import treat_app.web_service.service.dto.UserDto;

public interface UserService {

    UserDto create(UserDto user);

    UserDto getByid(Long id);

    UserDto update(UserDto userDto);

    void deleteById(Long id);
}