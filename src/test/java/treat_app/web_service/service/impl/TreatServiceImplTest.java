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

        TreatDto testedDto = service.create(insertedDto);
        //then
        assertThat(testedDto).isNotNull();
        assertThat(testedDto.getId()).isEqualTo(user.getId());
        assertThat(testedDto).isEqualToComparingOnlyGivenFields(insertedDto, "name", "amount", "increaseBy", "pic", "userId");
    }

    @Test(expected = NotFoundException.class)
    public void create_treatWithNotFoundUserId_throwsNotFoundException() {
        //given
        TreatDto insertedDto = ObjectFactory.TreatDto_userId(null);
        insertedDto.setId(null);
        //when-then
        when(userRepo.findByIdOrThrow(insertedDto.getUserId())).thenThrow(NotFoundException.class);
        service.create(insertedDto);
    }

    @Test
    public void createMany_listOfTreatsWithNullIdsAndValidUserIdAllOfTheTreatsHaveTheSameUserIds_returnsListOfTreatsWithIdsFromDb() {
        //given
        int listSize = 3;
        String[] treatNames = {"one", "two", "three"};
        User user = ObjectFactory.User();
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        List<Treat> treats = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId(null);
            treat.setName(treatNames[i]);
            treats.add(treat);
        }
        List<Treat> savedTreats = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Treat treat = ObjectFactory.Treat_user(user);
            treat.setId((long) i);
            treat.setName(treatNames[i]);
            treats.add(treat);
        }
        List<TreatDto> savedDtos = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId((long) i);
            treatDto.setName(treatNames[i]);
            savedDtos.add(treatDto);
        }
        //when
        when(treatMapper.toTreatEntities(savedDtos)).thenReturn(treats);
        when(treatRepo.saveAll(treats)).thenReturn(savedTreats);
        when(treatMapper.toTreatDtos(savedTreats)).thenReturn(savedDtos);

        List<TreatDto> testedDtos = service.createMany(insertedList);
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
        int listSize = 3;
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId((long) i);
            treatDto.setId(null);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when-then
        service.createMany(insertedList);
    }

    @Test(expected = WrongInputException.class)
    public void createMany_oneUserIdDoesNotMatchTheOthers_throwsWrongInputException() {
        //given
        int listSize = 3;
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        TreatDto lastDto = ObjectFactory.TreatDto_userId(2L);
        lastDto.setId(null);
        insertedList.set(new Random().nextInt(3), lastDto);
        //when-then
        service.createMany(insertedList);
    }

    @Test(expected = NotFoundException.class)
    public void createMany_theUserIdIsNotInDb_throwsNotFoundException() {
        //given
        int listSize = 3;
        String[] treatNames = {"one", "two", "three"};
        List<TreatDto> insertedList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
            treatDto.setId(null);
            treatDto.setName(treatNames[i]);
            insertedList.add(treatDto);
        }
        //when-then
        when(userRepo.findByIdOrThrow(1L)).thenThrow(NotFoundException.class);
        service.createMany(insertedList);
    }
}