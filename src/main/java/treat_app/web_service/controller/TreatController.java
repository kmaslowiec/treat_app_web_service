package treat_app.web_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/treat")
@AllArgsConstructor
public class TreatController {

    private static final String USER_PATH = "api/treat";
    private TreatService treatService;

    @PostMapping
    public ResponseEntity<TreatDto> create(@Validated @RequestBody TreatDto treatDto) throws URISyntaxException {
        if (treatDto.getId() != null) {
            return new ResponseEntity<>(treatDto, HeaderFactory.idHasToBeNull(), HttpStatus.BAD_REQUEST);
        } else if (treatDto.getUserId() == null) {
            return new ResponseEntity<>(treatDto, HeaderFactory.UserIdCantBeNull(), HttpStatus.BAD_REQUEST);
        }
        TreatDto savedTreat = treatService.createTreat(treatDto);
        return ResponseEntity.created(new URI(USER_PATH + "/" + savedTreat.getId())).body(savedTreat);
    }

    @PostMapping("many")
    public ResponseEntity<List<TreatDto>> createMany(@Validated @RequestBody List<TreatDto> treats) throws URISyntaxException {
        for (TreatDto i : treats) {
            if (i.getId() != null) {
                return new ResponseEntity<>(treats, HeaderFactory.idHasToBeNull(), HttpStatus.BAD_REQUEST);
            } else if (i.getUserId() == null) {
                return new ResponseEntity<>(treats, HeaderFactory.UserIdCantBeNull(), HttpStatus.BAD_REQUEST);
            }
        }
        List<TreatDto> savedTreats = treatService.createTreats(treats);
        return ResponseEntity.created(new URI(USER_PATH + "/" + treats.size())).body(savedTreats);
    }

    @PutMapping("many")
    public ResponseEntity<List<TreatDto>> update(@Validated @RequestBody List<TreatDto> treats) {
        for (TreatDto i : treats) {
            if (i.getId() == null) {
                return new ResponseEntity<>(treats, HeaderFactory.idCantBeNull(), HttpStatus.BAD_REQUEST);
            } else if (i.getUserId() == null) {
                return new ResponseEntity<>(treats, HeaderFactory.UserIdCantBeNull(), HttpStatus.BAD_REQUEST);
            }
        }
        List<TreatDto> updatedTreats = treatService.updateTreats(treats);
        return ResponseEntity.ok(updatedTreats);
    }

    @GetMapping("{id}")
    public ResponseEntity<TreatDto> read(@PathVariable long id) {
        TreatDto treatInDb = treatService.getTreatById(id);
        return ResponseEntity.ok(treatInDb);
    }

    @GetMapping("many")
    public ResponseEntity<List<TreatDto>> readMany(@Validated @RequestParam List<Long> ids) {
        List<TreatDto> treatsDto = treatService.getTreatsByIds(ids);
        return new ResponseEntity<>(treatsDto, HttpStatus.OK);
    }

    @GetMapping("many/{userId}")
    public ResponseEntity<List<TreatDto>> readManyByUserId(@PathVariable long userId) {
        List<TreatDto> treatsDto = treatService.getAllTreatsByUserId(userId);
        return new ResponseEntity<>(treatsDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteById(@PathVariable long id) {
        treatService.deleteTreatById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteManyByIds(@RequestParam List<Long> ids) {
        treatService.deleteTreatsByIds(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("many/{userId}")
    public ResponseEntity<Long> deleteManyByUserId(@PathVariable long userId) {
        treatService.deleteTreatsByUser(userId);
        return null;
    }
}