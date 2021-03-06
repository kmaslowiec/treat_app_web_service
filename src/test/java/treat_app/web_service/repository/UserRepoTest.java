package treat_app.web_service.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import treat_app.web_service.ObjectFactory;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;
import treat_app.web_service.exceptions.NotFoundException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    @Test
    public void findByIdThrow_validLongId_returnValidUser() {
        //given
        User user = ObjectFactory.User();
        user.setId(null);
        Treat treatInDb = ObjectFactory.Treat_user(user);
        treatInDb.setId(null);
        user.setTreats(Collections.singletonList(treatInDb));

        //when
        User userInDb = entityManager.persistAndFlush(user);
        User tested = userRepo.findByIdOrThrow(userInDb.getId());

        //then
        assertThat(tested).isNotNull();
        assertThat(tested).isEqualToComparingFieldByField(userInDb);
    }

    @Test(expected = NotFoundException.class)
    public void findQuestionByIdOrThrow_ShouldThrowException_WhenInvalidIdIsPassed() {
        //when-given
        userRepo.findByIdOrThrow(1L);
    }
}