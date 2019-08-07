package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.User;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.service.mapper.UserMapper;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepo.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getByid(Long id) {
        User user = userRepo.findByIdOrThrow(id);
        return userMapper.toDto(user);
    }
}