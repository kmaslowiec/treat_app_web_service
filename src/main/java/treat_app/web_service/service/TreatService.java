package treat_app.web_service.service;

import treat_app.web_service.service.dto.TreatDto;

public interface TreatService {

    TreatDto create(TreatDto insertDto);
}