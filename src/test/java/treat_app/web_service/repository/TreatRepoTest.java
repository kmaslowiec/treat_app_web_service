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

import java.util.ArrayList;
import java.util.Arrays;
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
    public void findAllByUser_dbHasTreatsAssignedToUser_returnsTreatsOfTheUser() {
        //given
        User user = ObjectFactory.User();
        Treat treat = ObjectFactory.Treat_user(user);
        Treat treat1 = ObjectFactory.Treat_id_name_user(2L, "gluttony", user);
        Treat treat2 = ObjectFactory.Treat_id_name_user(3L, "greed", user);
        Treat treat3 = ObjectFactory.Treat_id_name_user(4L, "sloth", user);
        Treat treat4 = ObjectFactory.Treat_id_name_user(5L, "wrath", user);
        Treat treat5 = ObjectFactory.Treat_id_name_user(6L, "envy", user);
        Treat treat6 = ObjectFactory.Treat_id_name_user(7L, "pride", user);

        //when
        entityManager.merge(user);
        List<Treat> treats = new ArrayList<>(Arrays.asList(treat, treat1, treat2, treat3, treat4, treat5, treat6));
        List<Treat> treatsInDb = new ArrayList<>();
        treats.forEach(a -> treatsInDb.add(entityManager.merge(a)));

        List<Treat> tested = treatRepo.findAllByUserId(user.getId());

        //then
        assertThat(tested).isNotNull();
        assertThat(tested).isNotEmpty();
        for (int i = 0; i < tested.size(); i++) {
            assertThat(tested.get(i)).isEqualToComparingFieldByField(treatsInDb.get(i));
        }
        assertThat(tested).hasSameSizeAs(treatsInDb);
    }

    @Test
    public void findAllByUser_userIsNotFound_returnsEmptyList() {
        //when
        List<Treat> tested = treatRepo.findAllByUserId(0);

        //then
        assertThat(tested).isEmpty();
        assertThat(tested).isNotNull();
    }
}