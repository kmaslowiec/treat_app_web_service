package treat_app.web_service.service.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMapperImpl.class, TreatMapperImpl.class})
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void toDomain_correctDto_returnValidUserEntity() {
        //given
        UserDto userDto = ObjectFactory.UserDto();
        TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
        TreatDto treatDto1 = ObjectFactory.TreatDto_userId(2L);
        userDto.setTreatDtos(Arrays.asList(treatDto, treatDto1));
        Treat treat = ObjectFactory.Treat_user(ObjectFactory.User());
        Treat treat1 = ObjectFactory.Treat_id_name_user(2L, "savings", ObjectFactory.User());

        //when
        User result = userMapper.toEntity(userDto);
        result.setTreats(Arrays.asList(treat, treat1));
        //then
        assertThat(result).isNotEqualTo(userDto);
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getUserLogin()).isEqualTo(userDto.getUserLogin());
        assertThat(result.getTreats()).containsSequence(treat, treat1);
        assertThat(result.getTreats()).hasSameSizeAs(userDto.getTreatDtos());
    }

    @Test
    public void toDomain_nullDto_returnsNull() {
        //when
        User result = userMapper.toEntity(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toDto_correctEntity_returnsValidDto() {
        //given
        User user = ObjectFactory.User();
        Treat treat = ObjectFactory.Treat_user(ObjectFactory.User());
        Treat treat1 = ObjectFactory.Treat_id_name_user(2L, "savings", ObjectFactory.User());
        user.setTreats(Arrays.asList(treat, treat1));
        TreatDto treatDto = ObjectFactory.TreatDto_userId(1L);
        TreatDto treatDto1 = ObjectFactory.TreatDto_userId(2L);

        //when
        UserDto result = userMapper.toDto(user);
        result.setTreatDtos(Arrays.asList(treatDto, treatDto1));

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(user);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUserLogin()).isEqualTo(user.getUserLogin());
        assertThat(result.getTreatDtos()).containsSequence(treatDto, treatDto1);
        assertThat(result.getTreatDtos()).hasSameSizeAs(user.getTreats());
    }

    @Test
    public void toDto_nullEntity_returnsNull() {
        //when
        UserDto result = userMapper.toDto(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toTreatDtos_treatEntieties_returnTreatDtos() {
        //given
        List<Treat> entieties = new ArrayList<>(Arrays.asList(ObjectFactory.Treat_user(ObjectFactory.User()), ObjectFactory.Treat_id_name_user(2L, "bob", ObjectFactory.User())));

        //when
        List<TreatDto> result = userMapper.toTreatDtos(entieties);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(entieties);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i)).isEqualToComparingOnlyGivenFields(entieties.get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(result.get(i).getUserId()).isEqualTo(entieties.get(i).getUser().getId());
        }
    }

    @Test
    public void toTreatDtos_null_returnsNull() {
        //when
        List<TreatDto> result = userMapper.toTreatDtos(null);

        //then
        assertThat(result).isNull();
    }

    @Test
    public void toTreatDtos_emptyList_returnsEmptyList() {
        //when
        List<TreatDto> result = userMapper.toTreatDtos(Collections.emptyList());

        //then
        assertThat(result).isEmpty();
    }

    @Test
    public void toTreatEntities_treatDtos_returnTreatEntities() {
        //given
        List<TreatDto> dtos = new ArrayList<>(Arrays.asList(ObjectFactory.TreatDto_userId(ObjectFactory.User().getId()), ObjectFactory.TreatDto_id_name_userId(2L, "bob", ObjectFactory.User().getId())));

        //when
        List<Treat> result = userMapper.toTreatEntities(dtos);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSameSizeAs(dtos);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i)).isEqualToComparingOnlyGivenFields(dtos.get(i), "id", "name", "amount", "increaseBy", "pic");
            assertThat(result.get(i).getUser().getId()).isEqualTo(dtos.get(i).getUserId());
        }
    }
}