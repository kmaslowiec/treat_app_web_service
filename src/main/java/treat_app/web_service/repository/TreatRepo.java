package treat_app.web_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.entity.User;

import java.util.List;

@Repository
public interface TreatRepo extends CrudRepository<Treat, Long> {

    //List<Treat> saveTreats(Iterable<Treat> treats);


    @Override
    <S extends Treat> Iterable<S> saveAll(Iterable<S> entities);

    List<Treat> findAllByUser(User users);
}