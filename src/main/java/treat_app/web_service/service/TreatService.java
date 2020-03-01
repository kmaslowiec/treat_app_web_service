package treat_app.web_service.service;

import treat_app.web_service.service.dto.TreatDto;

import java.util.List;

public interface TreatService {

    TreatDto createTreat(TreatDto insertDto);

    List<TreatDto> createTreats(List<TreatDto> treatsDto);

    List<TreatDto> updateTreats(List<TreatDto> treatsDto);

    TreatDto getTreatById(long id);

    List<TreatDto> getTreatsByIds(List<Long> ids);

    List<TreatDto> getAllTreatsByUserId(long userId);

    void deleteTreatById(long id);

    boolean deleteTreatsByIds(List<Long> ids);
}