package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.service.mapper.UserMapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private TreatRepo treatRepo;
    private UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        if (Objects.isNull(userDto.getTreatDtos())) {
            userDto.setTreatDtos(Collections.emptyList());
        }
        List<Treat> treats = userMapper.toTreatEntities(userDto.getTreatDtos());
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepo.save(user);
        treats.forEach(a -> a.setUser(savedUser));
        List<Treat> savedTreats = (List<Treat>) treatRepo.saveAll(treats);
        UserDto returnedDto = userMapper.toDto(savedUser);
        List<TreatDto> treatDtos = userMapper.toTreatDtos(savedTreats);
        returnedDto.setTreatDtos(treatDtos);

        return returnedDto;
    }

    @Override
    public UserDto getByid(Long id) {
        User userFromDb = userRepo.findByIdOrThrow(id);
        List<Treat> treats = treatRepo.findAllByUser(userFromDb);
        List<TreatDto> treatDtos = userMapper.toTreatDtos(treats);
        UserDto returnedDto = userMapper.toDto(userFromDb);
        returnedDto.setTreatDtos(treatDtos);

        return returnedDto;
    }
}