package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.service.mapper.UserMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) { //v2
        List<Treat> treats = userMapper.toTreatEntities(userDto.getTreatDtos());
        User user = userMapper.toEntity(userDto);
        user.setTreats(treats);

        User savedUser = userRepo.save(user);

        UserDto returnedDto = userMapper.toDto(savedUser);
        List<TreatDto> treatDtos = userMapper.toTreatDtos(savedUser.getTreats());
        returnedDto.setTreatDtos(treatDtos);

        return returnedDto;
    }

    @Override
    public UserDto getByid(Long id) { //v2
        User userFromDb = userRepo.findByIdOrThrow(id);

        List<TreatDto> treatDtos = userMapper.toTreatDtos(userFromDb.getTreats());
        UserDto returnedDto = userMapper.toDto(userFromDb);
        returnedDto.setTreatDtos(treatDtos);

        return returnedDto;
    }
}