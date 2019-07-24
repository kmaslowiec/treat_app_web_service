package treat_app.web_service.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.UserDto;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMapperImpl.class})
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Before
    public void setUp() {

    }

    @Test
    public void toDomain_correctDto_returnValidUserEntity() {
        //given
        UserDto userDto = ObjectFactory.UserDto();
        Treat treat = ObjectFactory.Treat_user(ObjectFactory.User());
        Treat treat1 = ObjectFactory.Treat_id_name_user(2L, "savings", ObjectFactory.User());

        //when
        User result = userMapper.toEntity(userDto);
        result.setTreats(Arrays.asList(treat, treat1));
        //then
        assertThat(result).isNotEqualTo(userDto);
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getUserLogin()).isEqualTo(userDto.getUserLogin());
        //TODO how to test the list and how to implement it. ManyToOne, OneToMany read more
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

        //when
        UserDto result = userMapper.toDto(user);
        //result.setTreatDtos(Arrays.asList(treatDto, treatDto1));

        //given
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(user);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUserLogin()).isEqualTo(user.getUserLogin());
        //TODO how to test the list and how to implement it. ManyToOne, OneToMany read more
    }

    @Test
    public void toDto_nullEntity_returnsNull() {
        //when
        UserDto result = userMapper.toDto(null);

        //then
        assertThat(result).isNull();
    }
}