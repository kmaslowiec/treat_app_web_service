package treat_app.web_service.service;

import treat_app.web_service.entity.User;

import java.util.List;

public interface UserService {

    User create(User user);

    List<User> getAll();

    User getByid(Long id);
}