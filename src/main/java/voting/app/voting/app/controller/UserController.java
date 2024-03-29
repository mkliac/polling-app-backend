package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.app.voting.app.aop.ExtractUser;
import voting.app.voting.app.dto.UserDto;
import voting.app.voting.app.model.User;
import voting.app.voting.app.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin
@SecurityRequirement(name = "BEARER TOKEN")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(
            tags = "User",
            summary = "Get User",
            description = "This endpoint is used to get the user")
    @ApiResponse(responseCode = "200", description = "Return the user")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<UserDto> getUser(@Parameter(hidden = true) User user) {
        return new ResponseEntity<>(userService.getUser(user.getId()), HttpStatus.OK);
    }
}
