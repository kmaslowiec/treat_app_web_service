package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.User;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    public User create(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getByid(Long id) {
        return userRepo.findByIdOrThrow(id);
    }
}
