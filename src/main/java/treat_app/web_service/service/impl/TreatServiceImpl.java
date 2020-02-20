package treat_app.web_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;

@Service
@AllArgsConstructor
public class TreatServiceImpl implements TreatService {

    @Override
    public TreatDto create(TreatDto insertDto) {
        return null;
    }
}