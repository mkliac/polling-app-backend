package voting.app.voting.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.app.voting.app.aop.ExtractUser;
import voting.app.voting.app.model.User;
import voting.app.voting.app.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ExtractUser(fieldName = "user")
    public ResponseEntity<User> saveUser(User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }
}
