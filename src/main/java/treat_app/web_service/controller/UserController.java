package treat_app.web_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private static final String USER_PATH = "api/user";
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto userDto) throws URISyntaxException {
        UserDto savedUser = userService.create(userDto);

        return ResponseEntity.created(new URI(USER_PATH + "/" + savedUser.getId())).body(savedUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> read(@PathVariable Long id) {
        UserDto fromDb = userService.getByid(id);
        return ResponseEntity.ok(fromDb);
    }

    @GetMapping()
    public String readTest() {
        return "server works";
    }
}