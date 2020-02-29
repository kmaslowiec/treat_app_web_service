package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.exceptions.NotFoundException;
import treat_app.web_service.exceptions.WrongInputException;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.mapper.TreatMapper;
import treat_app.web_service.util.MyStrings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


@Service
@AllArgsConstructor
public class TreatServiceImpl implements TreatService {

    UserRepo userRepo;

    TreatRepo treatRepo;

    TreatMapper treatMapper;

    @Override
    public TreatDto createTreat(TreatDto insertDto) {
        userRepo.findByIdOrThrow(insertDto.getUserId());
        Treat treat = treatMapper.toEntity(insertDto);
        Treat savedTreat = treatRepo.save(treat);
        return treatMapper.toDto(savedTreat);
    }

    @Override
    public List<TreatDto> createTreats(List<TreatDto> treatsDto) {
        return isCreateOrUpdate(treatsDto, true);
    }

    @Override
    public List<TreatDto> updateTreats(List<TreatDto> treatsDto) {
        return isCreateOrUpdate(treatsDto, false);
    }

    @Override
    public TreatDto getTreatById(long id) {
        Treat treatInDb = treatRepo.findByIdOrThrow(id);
        return treatMapper.toDto(treatInDb);
    }

    @Override
    public List<TreatDto> getTreatsByIds(List<Long> ids) {
        checkIfIdsAreInDb(ids);
        List<Treat> treats = treatRepo.findAllById(ids);
        return treatMapper.toTreatDtos(treats);
    }

    @Override
    public List<TreatDto> getAllTreatsByUserId(long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(MyStrings.EXCEPTION_NO_ID + userId);
        }
        List<Treat> treatsInDb = treatRepo.findAllByUserId(userId);
        return treatMapper.toTreatDtos(treatsInDb);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void checkIfTreatsIsEmpty(List<TreatDto> treats) {
        if (treats.size() == 0) {
            throw new WrongInputException(MyStrings.EXCEPTION_LIST_IS_EMPTY);
        }
    }

    private void checkIfUserIdsVary(List<TreatDto> treatsDto) {
        long size = treatsDto.stream()
                .filter(distinctByKey(TreatDto::getUserId))
                .count();
        if (size != 1) {
            throw new WrongInputException(MyStrings.EXCEPTION_USER_IDS_VARY);
        }
    }

    private void checkIfIdsAreInDb(List<Long> ids) {
        List<Long> notInDb = new ArrayList<>();
        for (long i : ids) {
            if (!treatRepo.existsById(i)) {
                notInDb.add(i);
            }
        }
        if (notInDb.size() != 0) {
            String msg = notInDb.size() > 1 ? MyStrings.EXCEPTION_IDS_NOT_IN_DB : MyStrings.EXCEPTION_IDS_NOT_IN_DB;
            throw new NotFoundException(String.format(msg, notInDb.toString().substring(1, notInDb.toString().length() - 1)));
        }
    }

    private List<TreatDto> isCreateOrUpdate(List<TreatDto> treatsDto, boolean confirm) {
        checkIfTreatsIsEmpty(treatsDto);
        checkIfUserIdsVary(treatsDto);
        if (isCreate(confirm)) {
            userRepo.findByIdOrThrow(treatsDto.get(0).getUserId());
        } else {
            treatsDto.forEach(a -> treatRepo.findByIdOrThrow(a.getId()));
        }
        List<Treat> treats = treatMapper.toTreatEntities(treatsDto);
        List<Treat> savedTreats = treatRepo.saveAll(treats);
        return treatMapper.toTreatDtos(savedTreats);
    }

    private boolean isCreate(boolean decide) {
        return decide;
    }
}