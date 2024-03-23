package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.app.voting.app.aop.ExtractUser;
import voting.app.voting.app.model.User;
import voting.app.voting.app.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin
@SecurityRequirement(name = "BEARER TOKEN")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(
            tags = "User",
            summary = "Save User",
            description = "This endpoint is used to save and login a user")
    @ApiResponse(responseCode = "200", description = "Return the logged in user")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<User> saveUser(User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }
}
