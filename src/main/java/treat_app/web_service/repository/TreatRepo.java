package treat_app.web_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;

import java.util.List;

@Repository
public interface TreatRepo extends CrudRepository<Treat, Long> {


    <S extends Treat> List<S> saveAll(Iterable<S> entities);

    List<Treat> findAllByUser(User user);
}