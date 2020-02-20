package treat_app.web_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/treat")
@AllArgsConstructor
public class TreatController {

    private static final String USER_PATH = "api/treat";
    private TreatService treatService;

    @PostMapping
    public ResponseEntity<TreatDto> addTreat(@Validated @RequestBody TreatDto treatDto) throws URISyntaxException {
        if (treatDto.getId() != null) {
            return new ResponseEntity<>(treatDto, HeaderFactory.idHasToBeNull(), HttpStatus.BAD_REQUEST);
        } else if (treatDto.getUserId() == null) {
            return new ResponseEntity<>(treatDto, HeaderFactory.UserIdCantBeNull(), HttpStatus.BAD_REQUEST);
        }
        TreatDto savedTreat = treatService.create(treatDto);
        return ResponseEntity.created(new URI(USER_PATH + "/" + savedTreat.getId())).body(savedTreat);
    }
}