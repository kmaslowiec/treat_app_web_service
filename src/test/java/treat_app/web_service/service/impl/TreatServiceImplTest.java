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
import treat_app.web_service.service.mapper.TreatMapper;

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
}