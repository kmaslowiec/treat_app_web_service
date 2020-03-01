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
import treat_app.web_service.exceptions.WrongInputException;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.mapper.TreatMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TreatServiceImplTest {

    @Mock
    TreatMapper treatMapper;

    @Mock
    TreatRepo treatRepo;

    @Mock
    UserRepo userRepo;

    @InjectMocks
    TreatServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_validTreatWithNullIdAndUserID_returnDtoWithIdFromDb() {
        //given
        TreatDto insertedDto = ObjectFactory.TreatDto_userId(1L);
        insertedDto.setId(null);
        User user = ObjectFactory.User();
        Treat treat = ObjectFactory.Treat_user(user);
        treat.setId(null);
        Treat savedTreat = ObjectFactory.Treat_user(user);
        TreatDto savedDto = ObjectFactory.TreatDto_userId(user.getId());
        //when
        when(userRepo.findByIdOrThrow(insertedDto.getUserId())).thenReturn(user);
        when(treatMapper.toEntity(insertedDto)).thenReturn(treat);
        when(treatRepo.save(treat)).thenReturn(savedTreat);
        when(treatMapper.toDto(savedTreat)).thenReturn(savedDto);

        TreatDto testedDto = service.createTreat(insertedDto);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getId()).isEqualTo(user.getId());
        assertThat(testedDto).isEqualToComparingOnlyGivenFields(insertedDto, "name", "amount", "increaseBy", "pic", "userId");
    }

    @Test(expected = NotFoundException.class)
    public void create_treatWithNotFoundUserId_throwsNotFoundException() {
        //given
        TreatDto insertedDto = ObjectFactory.TreatDto_userId(null);
        insertedDto.setId(1L);

        //when-then
        when(userRepo.findByIdOrThrow(insertedDto.getUserId())).thenThrow(NotFoundException.class);
        service.createTreat(insertedDto);
    }

    @Test
    public void createMany_listOfTreatsWithNullIdsAndValidUserIdAllOfTheTreatsHaveTheSameUserIds_returnsListOfTreatsWithIdsFromDb() {
        //given
        String[] treatNames = {"one", "two", "three"};
        User user = ObjectFactory.User();
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        List<Treat> treats = new ArrayList<>();
        for (String treatName : treatNames) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId(null);
            treat.setName(treatName);
            treats.add(treat);
        }
        List<Treat> savedTreats = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId((long) i);
            treat.setName(treatNames[i]);
            treats.add(treat);
        }
        List<TreatDto> savedDtos = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            savedDtos.add(treatDto);
        }
        //when
        when(treatMapper.toTreatEntities(savedDtos)).thenReturn(treats);
        when(treatRepo.saveAll(treats)).thenReturn(savedTreats);
        when(treatMapper.toTreatDtos(savedTreats)).thenReturn(savedDtos);

        List<TreatDto> testedDtos = service.createTreats(insertedList);
        //then
        assertThat(testedDtos).isNotNull();
        assertThat(testedDtos).hasSameSizeAs(insertedList);
        assertThat(testedDtos).isNotEqualTo(insertedList);
        for (int i = 0; i < insertedList.size(); i++) {
            assertThat(testedDtos.get(i).getId()).isNotNull();
            assertThat(testedDtos.get(i))
                    .isEqualToComparingOnlyGivenFields(insertedList.get(i),
                            "name", "amount", "increaseBy", "pic", "userId");
        }
    }

    @Test(expected = WrongInputException.class)
    public void createMany_theUserIdsAreAllDifferent_throwsWrongInputException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId((long) i);
            treatDto.setId(null);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when-then
        service.createTreats(insertedList);
    }

    @Test(expected = WrongInputException.class)
    public void createMany_oneUserIdDoesNotMatchTheOthers_throwsWrongInputException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        TreatDto lastDto = ObjectFactory.TreatDto_userId(2L);
        lastDto.setId(null);
        insertedList.set(new Random().nextInt(3), lastDto);
        //when-then
        service.createTreats(insertedList);
    }

    @Test(expected = NotFoundException.class)
    public void createMany_theUserIdIsNotInDb_throwsNotFoundException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        //when-then
        when(userRepo.findByIdOrThrow(insertedList.get(0).getUserId())).thenThrow(NotFoundException.class);
        service.createTreats(insertedList);
    }

    @Test(expected = WrongInputException.class)
    public void createMany_treatsAreEmpty_throwsWrongInputException() {
        //given
        List<TreatDto> insertedList = Collections.emptyList();
        //when-then
        service.createTreats(insertedList);
    }

    @Test
    public void updateMany_listOfTreatsWithNotNullIdsAndValidUserIdAllOfTheTreatsHaveTheSameUserIds_returnsListOfTreatsWithIdsFromDb() {
        //given
        String[] treatNames = {"one", "two", "three"};
        User user = ObjectFactory.User();
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        List<Treat> treats = new ArrayList<>();
        for (String treatName : treatNames) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId(null);
            treat.setName(treatName);
            treats.add(treat);
        }
        List<Treat> savedTreats = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId((long) i);
            treat.setName(treatNames[i]);
            treats.add(treat);
        }
        List<TreatDto> savedDtos = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            savedDtos.add(treatDto);
        }
        //when
        when(treatMapper.toTreatEntities(savedDtos)).thenReturn(treats);
        when(treatRepo.saveAll(treats)).thenReturn(savedTreats);
        when(treatMapper.toTreatDtos(savedTreats)).thenReturn(savedDtos);

        List<TreatDto> testedDtos = service.updateTreats(insertedList);
        //then
        assertThat(testedDtos).isNotNull();
        assertThat(testedDtos).hasSameSizeAs(insertedList);
        for (int i = 0; i < insertedList.size(); i++) {
            assertThat(testedDtos.get(i).getId()).isNotNull();
            assertThat(testedDtos.get(i))
                    .isEqualToComparingOnlyGivenFields(insertedList.get(i),
                            "id", "name", "amount", "increaseBy", "pic", "userId");
        }
    }

    @Test(expected = WrongInputException.class)
    public void updateMany_theUserIdsAreAllDifferent_throwsWrongInputException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId((long) i);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when-then
        service.updateTreats(insertedList);
    }

    @Test(expected = WrongInputException.class)
    public void updateMany_oneUserIdDoesNotMatchTheOthers_throwsWrongInputException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (String treatName : treatNames) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatName);
            insertedList.add(treatDto);
        }
        int random = new Random().nextInt(3);
        TreatDto lastDto = ObjectFactory.TreatDto_userId(2L);
        lastDto.setId((long) random + 1);
        insertedList.set(random, lastDto);
        //when-then
        service.updateTreats(insertedList);
    }

    @Test(expected = NotFoundException.class)
    public void updateMany_theUserIdIsNotInDb_throwsNotFoundException() {
        //given
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when-then
        when(userRepo.findByIdOrThrow(insertedList.get(0).getUserId())).thenThrow(NotFoundException.class);
        service.createTreats(insertedList);
    }

    @Test(expected = WrongInputException.class)
    public void updateMany_treatsAreEmpty_throwsWrongInputException() {
        //given
        List<TreatDto> insertedList = Collections.emptyList();
        //when-then
        service.createTreats(insertedList);
    }

    @Test
    public void getTreatById_theIdIsInDb_returnsTreatDto() {
        //given
        User user = ObjectFactory.User();
        Treat treatFromDB = ObjectFactory.Treat_user(user);
        TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
        //when
        when(treatRepo.findByIdOrThrow(1L)).thenReturn(treatFromDB);
        when(treatMapper.toDto(treatFromDB)).thenReturn(treatDto);

        TreatDto testedDto = service.getTreatById(1L);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto)
                .isEqualToComparingOnlyGivenFields(treatFromDB, "id", "name", "amount", "increaseBy", "pic");
        assertThat(testedDto.getUserId()).isEqualTo(treatFromDB.getUser().getId());
    }

    @Test(expected = NotFoundException.class)
    public void getTreatById_theIdIsNotInDb_throwsNotFoundException() {
        //when-then
        when(treatRepo.findByIdOrThrow(1L)).thenThrow(NotFoundException.class);

        service.getTreatById(1L);
    }

    @Test
    public void getTreatsByIds_allIdsAreInDb_returnsTreatsDto() {
        //given
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        User user = ObjectFactory.User();
        String[] treatNames = {"one", "two", "three"};
        List<Treat> treatsFromDb = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId((long) i + 1);
            treat.setName(treatNames[i]);
            treatsFromDb.add(treat);
        }
        List<TreatDto> treatsDto = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(user.getId());
            treatDto.setId((long) i + 1);
            treatDto.setName(treatNames[i]);
            treatsDto.add(treatDto);
        }
        //when
        for (long i : ids) {
            when(treatRepo.existsById(i)).thenReturn(true);
        }
        when(treatRepo.findAllById(ids)).thenReturn(treatsFromDb);
        when(treatMapper.toTreatDtos(treatsFromDb)).thenReturn(treatsDto);

        List<TreatDto> retrievedDto = service.getTreatsByIds(ids);
        //then
        assertThat(retrievedDto).isNotNull();
        assertThat(retrievedDto).hasSameSizeAs(ids);
        for (int i = 0; i < ids.size(); i++) {
            assertThat(retrievedDto.get(i).getId()).isNotNull();
            assertThat(retrievedDto.get(i))
                    .hasFieldOrPropertyWithValue("id", retrievedDto.get(i).getId())
                    .hasFieldOrPropertyWithValue("name", retrievedDto.get(i).getName())
                    .hasFieldOrPropertyWithValue("amount", retrievedDto.get(i).getAmount())
                    .hasFieldOrPropertyWithValue("increaseBy", retrievedDto.get(i).getIncreaseBy())
                    .hasFieldOrPropertyWithValue("pic", retrievedDto.get(i).getPic())
                    .hasFieldOrPropertyWithValue("userId", retrievedDto.get(i).getUserId());
        }
    }


    @Test(expected = NotFoundException.class)
    public void getTreatsByIds_idIsNotInDb_throwsNotFoundException() {
        //given
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        //when-then
        service.getTreatsByIds(ids);
    }

    @Test
    public void getAllTreatsByUserId_userIdIsInDb_returnsTreatsDto() {
        //given
        User user = ObjectFactory.User();
        String[] treatNames = {"one", "two", "three"};
        List<Treat> treatsFromDb = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId((long) i + 1);
            treat.setName(treatNames[i]);
            treatsFromDb.add(treat);
        }
        List<TreatDto> returnedDtos = new ArrayList<>();
        for (int i = 0; i < treatNames.length; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            returnedDtos.add(treatDto);
        }
        when(userRepo.existsById(user.getId())).thenReturn(true);
        when(treatRepo.findAllByUserId(user.getId())).thenReturn(treatsFromDb);
        when(treatMapper.toTreatDtos(treatsFromDb)).thenReturn(returnedDtos);

        List<TreatDto> testedDto = service.getAllTreatsByUserId(user.getId());
        assertThat(testedDto).isNotNull();

        for (int i = 0; i < treatNames.length; i++) {
            assertThat(testedDto.get(i).getId()).isNotNull();
            assertThat(testedDto.get(i))
                    .hasFieldOrPropertyWithValue("id", testedDto.get(i).getId())
                    .hasFieldOrPropertyWithValue("name", testedDto.get(i).getName())
                    .hasFieldOrPropertyWithValue("amount", testedDto.get(i).getAmount())
                    .hasFieldOrPropertyWithValue("increaseBy", testedDto.get(i).getIncreaseBy())
                    .hasFieldOrPropertyWithValue("pic", testedDto.get(i).getPic())
                    .hasFieldOrPropertyWithValue("userId", testedDto.get(i).getUserId());
        }
    }

    @Test(expected = NotFoundException.class)
    public void getAllTreatsByUserId_userIdIsNotDb_throwsNotFoundException() {
        //given
        User user = ObjectFactory.User();
        //when-then
        when(userRepo.existsById(user.getId())).thenReturn(false);
        service.getAllTreatsByUserId(user.getId());
    }

    @Test
    public void deleteUserById_treatIdIsInDb_deleteTheEntityFromDb() {
        //given
        Treat treatInDb = ObjectFactory.Treat_user(ObjectFactory.User());
        long id = 1L;
        //when-then
        when(treatRepo.findByIdOrThrow(id)).thenReturn(treatInDb);
        service.deleteTreatById(id);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserById_idIsNotInDb_throwsNotFoundException() {
        //given
        Treat treatInDb = ObjectFactory.Treat_user(ObjectFactory.User());
        long id = 1L;
        //when-then
        when(treatRepo.findByIdOrThrow(id)).thenThrow(NotFoundException.class);
        service.deleteTreatById(id);
    }
}