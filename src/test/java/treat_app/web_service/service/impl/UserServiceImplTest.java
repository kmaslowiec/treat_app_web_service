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
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;
import treat_app.web_service.service.mapper.TreatMapper;
import treat_app.web_service.service.mapper.UserMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TreatMapper treatMapper;

    @Mock
    private TreatRepo treatRepo;

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
        UserDto enteredDto = ObjectFactory.UserDto();
        enteredDto.setTreatDtos(treatsInEnteredDto);
        User user = ObjectFactory.User();
        List<Treat> treatsFromUser = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(user), ObjectFactory.Treat_id_name_user(2L, "second", user)));
        List<Treat> returnedTreats = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(user), ObjectFactory.Treat_id_name_user(2L, "second", user)));
        user.setTreats(treatsFromUser);

        User savedUser = ObjectFactory.User();
        savedUser.setTreats(returnedTreats);
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(treatMapper.toTreatEntities(enteredDto.getTreatDtos())).thenReturn(treatsFromUser);
        when(userMapper.toEntity(enteredDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(savedUser);
        when(treatRepo.saveAll(treatsFromUser)).thenReturn(returnedTreats);
        when(userMapper.toDto(savedUser)).thenReturn(returnedDto);
        when(treatMapper.toTreatDtos(savedUser.getTreats())).thenReturn(treatsInEnteredDto);
        UserDto testedDto = service.create(enteredDto);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).isEqualTo(enteredDto.getTreatDtos());
        for (int i = 0; i < testedDto.getTreatDtos().size(); i++) {
            assertThat(testedDto.getTreatDtos().get(i)).isEqualToComparingOnlyGivenFields(enteredDto.getTreatDtos().get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(testedDto.getTreatDtos().get(i).getUserId()).isEqualTo(enteredDto.getTreatDtos().get(i).getUserId());
        }
    }

    @Test
    public void create_userHasNullTreats_returnEmptyList() {
        //given
        UserDto enteredDto = ObjectFactory.UserDto();
        enteredDto.setTreatDtos(null);
        User user = ObjectFactory.User();
        List<Treat> treatsFromUser = Collections.emptyList();
        user.setTreats(treatsFromUser);
        User savedUser = ObjectFactory.User();
        UserDto returnedDto = ObjectFactory.UserDto();
        //when
        when(treatMapper.toTreatEntities(enteredDto.getTreatDtos())).thenReturn(treatsFromUser);
        when(userMapper.toEntity(enteredDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(returnedDto);
        when(treatMapper.toTreatDtos(savedUser.getTreats())).thenReturn(Collections.emptyList());

        UserDto testedDto = service.create(enteredDto);
        //then
        assertThat(testedDto.getTreatDtos()).isEmpty();
    }

    @Test
    public void getByid_validLongId_returnsUserDto() {
        //given
        User userFromDb = ObjectFactory.User();
        List<Treat> treatsFromDb = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(userFromDb), ObjectFactory.Treat_id_name_user(2L, "second", userFromDb)));
        List<TreatDto> dtosFromUser = new ArrayList<>(Arrays.asList(ObjectFactory.TreatDto_userId(1L), ObjectFactory.TreatDto_id_name_userId(2L, "second", 1L)));
        UserDto returnedDto = ObjectFactory.UserDto();
        returnedDto.setTreatDtos(dtosFromUser);
        //when
        when(userRepo.findByIdOrThrow(1L)).thenReturn(userFromDb);
        when(treatRepo.findAllByUser(userFromDb)).thenReturn(treatsFromDb);
        when(treatMapper.toTreatDtos(treatsFromDb)).thenReturn(dtosFromUser);
        when(userMapper.toDto(userFromDb)).thenReturn(returnedDto);

        UserDto testedDto = service.getByid(1L);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).hasSameSizeAs(treatsFromDb);
        for (int i = 0; i < testedDto.getTreatDtos().size(); i++) {
            assertThat(testedDto.getTreatDtos().get(i)).isEqualToComparingOnlyGivenFields(treatsFromDb.get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(testedDto.getTreatDtos().get(i).getUserId()).isEqualTo(treatsFromDb.get(i).getUser().getId());
        }
    }

    @Test(expected = NotFoundException.class)
    public void getByid_nullId_returnsUserDto() {
        //given
        when(userRepo.findByIdOrThrow(1L)).thenThrow(NotFoundException.class);
        //when-then
        service.getByid(1L);
    }

    @Test
    public void getByid_validLongId_userHasAnEmpytTreats() {
        //given
        User userFromDb = ObjectFactory.User();
        List<Treat> treatsFromDb = Collections.emptyList();
        List<TreatDto> dtosFromUser = Collections.emptyList();
        UserDto returnedDto = ObjectFactory.UserDto();
        returnedDto.setTreatDtos(dtosFromUser);
        //when
        when(userRepo.findByIdOrThrow(1L)).thenReturn(userFromDb);
        when(treatRepo.findAllByUser(userFromDb)).thenReturn(treatsFromDb);
        when(treatMapper.toTreatDtos(treatsFromDb)).thenReturn(dtosFromUser);
        when(userMapper.toDto(userFromDb)).thenReturn(returnedDto);

        UserDto tested = service.getByid(1L);
        //then
        assertThat(tested.getTreatDtos()).isEmpty();
    }

    @Test
    public void update_userDtoHasId_returnsUpdatedDto() {
        int numOfTreats = 3;
        //given
        User userInDb = ObjectFactory.User_id_login(1L, "different");
        UserDto insertedUserDto = ObjectFactory.UserDto();
        User insertedUser = ObjectFactory.User();
        UserDto savedUserDto = ObjectFactory.UserDto();
        User savedUser = ObjectFactory.User();
        List<TreatDto> treatDtos = new ArrayList<>();
        for (long i = 1; i <= numOfTreats; i++) {
            treatDtos.add(ObjectFactory.TreatDto_userId(i));
        }
        List<Treat> treats = new ArrayList<>();
        for (long i = 1; i <= numOfTreats; i++) {
            treats.add(ObjectFactory.Treat_id_user(i, insertedUser));
        }
        insertedUserDto.setTreatDtos(treatDtos);
        insertedUser.setTreats(treats);
        savedUser.setTreats(treats);
        savedUserDto.setTreatDtos(treatDtos);
        //when
        when(userRepo.findByIdOrThrow(insertedUserDto.getId())).thenReturn(userInDb);
        when(treatMapper.toTreatEntities(treatDtos)).thenReturn(treats);
        when(userMapper.toEntity(insertedUserDto)).thenReturn(insertedUser);
        when(userRepo.save(insertedUser)).thenReturn(savedUser);
        when(treatMapper.toTreatDtos(savedUser.getTreats())).thenReturn(savedUserDto.getTreatDtos());
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        //then
        UserDto testedDto = service.update(insertedUserDto);

        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).isEqualTo(insertedUserDto.getTreatDtos());
        for (int i = 0; i < testedDto.getTreatDtos().size(); i++) {
            assertThat(testedDto.getTreatDtos().get(i)).isEqualToComparingOnlyGivenFields(insertedUserDto.getTreatDtos().get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(testedDto.getTreatDtos().get(i).getUserId()).isEqualTo(insertedUserDto.getTreatDtos().get(i).getUserId());
        }
    }

    @Test(expected = NotFoundException.class)
    public void update_userIdNotFound_throwsNotFoundException() {
        //given
        UserDto insertedUserDto = ObjectFactory.UserDto();
        User insertedUser = ObjectFactory.User();
        //when-then
        when(userRepo.findByIdOrThrow(insertedUser.getId())).thenThrow(NotFoundException.class);
        service.update(insertedUserDto);
    }

    @Test
    public void update_userDtoHasNullTreats_returnsDtoWithAnEmptyList() {
        //given
        User userInDb = ObjectFactory.User_id_login(1L, "different");
        UserDto insertedUserDto = ObjectFactory.UserDto();
        insertedUserDto.setTreatDtos(null);
        User insertedUser = ObjectFactory.User();
        UserDto savedUserDto = ObjectFactory.UserDto();
        User savedUser = ObjectFactory.User();
        List<TreatDto> treatDtos = Collections.emptyList();
        List<Treat> treats = Collections.emptyList();
        insertedUserDto.setTreatDtos(treatDtos);
        insertedUser.setTreats(treats);
        savedUser.setTreats(treats);
        savedUserDto.setTreatDtos(treatDtos);
        //when
        when(userRepo.findByIdOrThrow(insertedUserDto.getId())).thenReturn(userInDb);
        when(treatMapper.toTreatEntities(treatDtos)).thenReturn(treats);
        when(userMapper.toEntity(insertedUserDto)).thenReturn(insertedUser);
        when(userRepo.save(insertedUser)).thenReturn(savedUser);
        when(treatMapper.toTreatDtos(savedUser.getTreats())).thenReturn(savedUserDto.getTreatDtos());
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);
        //then
        UserDto testedDto = service.update(insertedUserDto);

        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getTreatDtos()).isEmpty();
    }

    @Test
    public void deleteById_UserWithIdExists_UserIsDeleted() {
        //given
        UserDto insertedUserDto = ObjectFactory.UserDto();
        User userInDb = ObjectFactory.User();
        //when-then
        when(userRepo.findByIdOrThrow(insertedUserDto.getId())).thenReturn(userInDb);
        service.deleteById(insertedUserDto.getId());
        verify(userRepo).deleteById(userInDb.getId());
    }

    @Test(expected = NotFoundException.class)
    public void deleteById_UserWithIdDoesNotExist_throwNotFoundException() {
        //given
        UserDto insertedUserDto = ObjectFactory.UserDto();
        //when-then
        when(userRepo.findByIdOrThrow(insertedUserDto.getId())).thenThrow(NotFoundException.class);
        service.deleteById(insertedUserDto.getId());
        verify(userRepo).deleteById(insertedUserDto.getId());
    }
}