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
import org.springframework.web.bind.annotation.RestController;
import treat_app.web_service.service.UserService;
import treat_app.web_service.service.dto.UserDto;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private static final String USER_PATH = "api/user";
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody UserDto userDto) throws URISyntaxException {
        if (userDto.getId() != null) {
            return new ResponseEntity<>(userDto, HeaderFactory.idHasToBeNull(), HttpStatus.BAD_REQUEST);
        }
        UserDto savedUser = userService.createUser(userDto);

        return ResponseEntity.created(new URI(USER_PATH + "/" + savedUser.getId())).body(savedUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> read(@PathVariable Long id) {
        UserDto fromDb = userService.getUserByid(id);
        return ResponseEntity.ok(fromDb);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Validated @RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return new ResponseEntity<>(userDto, HeaderFactory.idCantBeNull(), HttpStatus.BAD_REQUEST);
        }
        UserDto updatedDto = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}