package treat_app.web_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.util.MyStrings;

import java.util.List;

@Repository
public interface TreatRepo extends CrudRepository<Treat, Long> {


    <S extends Treat> List<S> saveAll(Iterable<S> entities);

    default Treat findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(MyStrings.EXCEPTION_NO_ID + id));
    }

    @Override
    List<Treat> findAllById(Iterable<Long> list);

    List<Treat> findAllByUserId(long id);
}