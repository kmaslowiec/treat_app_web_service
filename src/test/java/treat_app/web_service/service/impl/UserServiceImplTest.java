package treat_app.web_service.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_newUser_validDto_returnsDtoWithSavedValues() {
        //given
        List<TreatDto> treatsInEnteredDto = new ArrayList<>(Arrays.asList(ObjectFactory.TreatDto_userId(1L), ObjectFactory.TreatDto_id_name_userId(2L, "second", 1L)));
        UserDto enterDto = ObjectFactory.UserDto();
        enterDto.setTreatDtos(treatsInEnteredDto);
        User user = ObjectFactory.User();
        List<Treat> treatsFromUser = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(user), ObjectFactory.Treat_id_name_user(2L, "second", user)));
        user.setTreats(treatsFromUser);

        User savedUser = ObjectFactory.User();
        UserDto returnedDto = ObjectFactory.UserDto();

        //when
        when(userMapper.toTreatEntities(enterDto.getTreatDtos())).thenReturn(treatsFromUser);
        when(userMapper.toEntity(enterDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(returnedDto);
        when(userMapper.toTreatDtos(savedUser.getTreats())).thenReturn(treatsInEnteredDto);

        UserDto testedDto = service.create(enterDto);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).isEqualTo(enterDto.getTreatDtos());
    }

    @Test
    public void getByid_validLong_returnsUserDto() {
        //given
        User userFromDb = ObjectFactory.User();
        List<Treat> treatDtosFromDb = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(userFromDb), ObjectFactory.Treat_id_name_user(2L, "second", userFromDb)));
        userFromDb.setTreats(treatDtosFromDb);
        List<TreatDto> dtosFromUser = new ArrayList<>(Arrays.asList(ObjectFactory.TreatDto_userId(1L), ObjectFactory.TreatDto_id_name_userId(2L, "second", 1L)));
        UserDto returnedDto = ObjectFactory.UserDto();
        returnedDto.setTreatDtos(dtosFromUser);

        //when
        when(userMapper.toTreatDtos(userFromDb.getTreats())).thenReturn(dtosFromUser);
        when(userRepo.findByIdOrThrow(1L)).thenReturn(userFromDb);
        when(userMapper.toDto(userFromDb)).thenReturn(returnedDto);

        UserDto testedDto = service.getByid(1L);

        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).isEqualTo(userFromDb.getTreats());
    }
}