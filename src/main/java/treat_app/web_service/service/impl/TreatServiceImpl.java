package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.repository.TreatRepo;
import treat_app.web_service.repository.UserRepo;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;
import treat_app.web_service.service.mapper.TreatMapper;

import java.util.List;


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

    @Override
    public List<TreatDto> createMany(List<TreatDto> treats) {
        return null;
    }
}