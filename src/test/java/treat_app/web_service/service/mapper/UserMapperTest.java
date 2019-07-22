package treat_app.web_service.service.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserMapperImpl.class})
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private User user;
    private Treat treat;
    private UserDto userDto;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setPassword("pass");
        user.setUserLogin("log");

        treat = new Treat();
        treat.setId(1L);
        treat.setName("hello");
        treat.setAmount(2.5f);
        treat.setPic(1);
        treat.setUser(user);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setPassword("pass");
        userDto.setUserLogin("log");
    }

    @Test
    public void toDomain_retrunUser_animalDtoIsValid() {
        User result = userMapper.toEntity(userDto);

        assertThat(result).isEqualTo(user);
    }
}