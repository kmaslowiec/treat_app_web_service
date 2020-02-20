package treat_app.web_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import treat_app.web_service.entity.Treat;
import treat_app.web_service.service.TreatService;
import treat_app.web_service.service.dto.TreatDto;

@RestController
@RequestMapping("api/treat")
@AllArgsConstructor
public class TreatController {

    private static final String USER_PATH = "api/treat";
    private TreatService treatService;

    @PostMapping
    public ResponseEntity<Treat> addTreat(@RequestBody TreatDto treatDto) {
        return null;
    }
}