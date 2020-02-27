package treat_app.web_service.service;

import treat_app.web_service.service.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto getUserByid(Long id);

    UserDto updateUser(UserDto userDto);

    void deleteUserById(Long id);
}