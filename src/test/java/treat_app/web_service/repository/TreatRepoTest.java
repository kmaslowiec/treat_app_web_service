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
        user.setId(null);
        entityManager.persistAndFlush(user);
        Treat treat = ObjectFactory.Treat_user(user);
        treat.setId(null);
        Treat treat1 = ObjectFactory.Treat_id_name_user(null, "gluttony", user);
        Treat treat2 = ObjectFactory.Treat_id_name_user(null, "greed", user);
        Treat treat3 = ObjectFactory.Treat_id_name_user(null, "sloth", user);
        Treat treat4 = ObjectFactory.Treat_id_name_user(null, "wrath", user);
        Treat treat5 = ObjectFactory.Treat_id_name_user(null, "envy", user);
        Treat treat6 = ObjectFactory.Treat_id_name_user(null, "pride", user);

        //when


        List<Treat> treats = new ArrayList<>(Arrays.asList(treat, treat1, treat2, treat3, treat4, treat5, treat6));
        List<Treat> treatsInDb = new ArrayList<>();
        treats.forEach(a -> treatsInDb.add(entityManager.persistAndFlush(a)));

        List<Treat> tested = treatRepo.findAllByUserId(user.getId());

        //then
        assertThat(tested).isNotNull();
        assertThat(tested).isNotEmpty();
        for (int i = 0; i < tested.size(); i++) {
            assertThat(tested.get(i)).isEqualToComparingFieldByField(treatsInDb.get(i));
        }
        assertThat(tested).hasSameSizeAs(treatsInDb);
        entityManager.flush();
    }

    @Test
    public void findAllByUser_userIsNotFound_returnsEmptyList() {
        //when
        List<Treat> tested = treatRepo.findAllByUserId(0);

        //then
        assertThat(tested).isEmpty();
        assertThat(tested).isNotNull();
    }

    @Test
    public void findByIdThrow_validLongId_returnValidUser() {
        //given
        User user = ObjectFactory.User();
        user.setId(null);
        Treat treat = ObjectFactory.Treat_user(user);
        treat.setId(null);


        //when
        entityManager.persistAndFlush(user);
        Treat treatInDb = entityManager.persistAndFlush(treat);
        Treat tested = treatRepo.findByIdOrThrow(treatInDb.getId());
        //then
        assertThat(tested).isNotNull();
        assertThat(tested).isEqualToComparingFieldByField(treatInDb);
    }

    @Test(expected = NotFoundException.class)
    public void findQuestionByIdOrThrow_ShouldThrowException_WhenInvalidIdIsPassed() {
        //when-given
        treatRepo.findByIdOrThrow(1L);
    }
}