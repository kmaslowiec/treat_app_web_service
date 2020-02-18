package treat_app.web_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import treat_app.web_service.entity.User;
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.util.MyStrings;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    default User findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(MyStrings.EXCEPTION_NO_ID + id));
    }

    @Override
    List<User> findAll();
}