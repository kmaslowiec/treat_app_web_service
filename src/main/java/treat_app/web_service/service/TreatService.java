package treat_app.web_service.service;

import treat_app.web_service.service.dto.TreatDto;

import java.util.List;

public interface TreatService {

    TreatDto create(TreatDto insertDto);

    List<TreatDto> createMany(List<TreatDto> treats);
}