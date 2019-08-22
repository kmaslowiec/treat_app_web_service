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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TreatRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TreatRepo treatRepo;

    @Test
    public void findAllByUser_dbHasTreatAssignedToUser_returnsTreatThatIsAssignedToUser() {
        //given
        User user = ObjectFactory.User();
        Treat treat = ObjectFactory.Treat_user(user);

        //when
        entityManager.merge(user);
        Treat treatInDb = entityManager.merge(treat);
        List<Treat> tested = treatRepo.findAllByUser(user);

        //then
        assertThat(tested).isNotNull();
        assertThat(tested).isNotEmpty();
        assertThat(tested.get(0)).isEqualToComparingFieldByField(treatInDb);
    }
}