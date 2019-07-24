package treat_app.web_service.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.dto.UserDto;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMapperImpl.class})
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private User user;
    private Treat treat;
    private Treat treat2;
    private TreatDto treatDto;
    private TreatDto treatDto1;
    private UserDto userDto;

    @Before
    public void setUp() {
        treat = new Treat();
        treat.setId(1L);
        treat.setName("hello");
        treat.setAmount(2.5f);
        treat.setPic(1);
        treat.setUser(user);

        treat2 = new Treat();
        treat2.setId(2L);
        treat2.setName("World");
        treat2.setAmount(3.5f);
        treat2.setPic(2);
        treat2.setUser(user);

        treatDto = new TreatDto();
        treatDto.setId(1L);
        treatDto.setName("hello");
        treatDto.setAmount(2.5f);
        treatDto.setPic(1);
        treatDto.setUserId(1L);

        treatDto1 = new TreatDto();
        treatDto1.setId(2L);
        treatDto1.setName("World");
        treatDto1.setAmount(6.0f);
        treatDto1.setPic(2);
        treatDto1.setUserId(1L);

        treat2 = new Treat();
        treat2.setId(2L);
        treat2.setName("World");
        treat2.setAmount(3.5f);
        treat2.setPic(2);
        treat2.setUser(user);

        user = new User();
        user.setId(1L);
        user.setPassword("pass");
        user.setUserLogin("log");
        user.setTreats(Arrays.asList(treat, treat2));

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("pass");
        userDto.setUserLogin("log");
        userDto.setTreatDtos(Arrays.asList(treatDto, treatDto1));
    }

    @Test
    public void toDomain_correctDto_returnValidUserEntity() {
        /*the variables should be init here, would be improved after builders and facotry is added.*/

        //when
        User result = userMapper.toEntity(userDto);
        result.setTreats(Arrays.asList(treat, treat2));
        //then
        assertThat(result).isNotEqualTo(userDto);
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getUserLogin()).isEqualTo(userDto.getUserLogin());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
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
        //when
        UserDto result = userMapper.toDto(user);
        result.setTreatDtos(Arrays.asList(treatDto, treatDto1));

        //given
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(user);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUserLogin()).isEqualTo(user.getUserLogin());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getTreatDtos()).hasSameSizeAs(user.getTreats());
    }

    @Test
    public void toDto_nullEntity_returnsNull() {
        //when
        UserDto result = userMapper.toDto(null);

        //then
        assertThat(result).isNull();
    }
}