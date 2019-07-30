package treat_app.web_service.service;

import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    List<User> getAll();

    User getByid(Long id);
}