package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.exceptions.WrongInputException;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.mapper.TreatMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TreatServiceImpl implements TreatService {

    UserRepo userRepo;

    TreatRepo treatRepo;

    TreatMapper treatMapper;

    @Override
    public TreatDto create(TreatDto insertDto) {
        userRepo.findByIdOrThrow(insertDto.getUserId());
        Treat treat = treatMapper.toEntity(insertDto);
        Treat savedTreat = treatRepo.save(treat);
        return treatMapper.toDto(savedTreat);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<TreatDto> createMany(List<TreatDto> treatsDto) {
        List<TreatDto> list = treatsDto.stream().filter(distinctByKey(TreatDto::getUserId)).collect(Collectors.toList());
        if (list.size() != 1) {
            throw new WrongInputException("The userIds have to be the same");
        }
        userRepo.findByIdOrThrow(treatsDto.get(0).getUserId());
        List<Treat> treats = treatMapper.toTreatEntities(treatsDto);
        List<Treat> savedTreats = treatRepo.saveAll(treats);
        return treatMapper.toTreatDtos(savedTreats);
    }
}