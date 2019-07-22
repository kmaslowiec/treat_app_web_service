package treat_app.web_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import treat_app.web_service.entity.User;
import treat_app.web_service.service.UserService;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<User> read(@PathVariable Long id) {
        User user = userService.getByid(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping()
    public String readTest() {
        return "server works";
    }
}