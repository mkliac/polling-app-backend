package polling.app.polling.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polling.app.polling.app.aop.ExtractUser;
import polling.app.polling.app.dto.user.UserDto;
import polling.app.polling.app.dto.user.UserWithDetailDto;
import polling.app.polling.app.model.user.User;
import polling.app.polling.app.service.UserService;

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
            description = "This endpoint is used to get the current user")
    @ApiResponse(responseCode = "200", description = "Return the user")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<UserDto> getUser(@Parameter(hidden = true) User user) {
        return new ResponseEntity<>(userService.getUser(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @Operation(
            tags = "User",
            summary = "Get User",
            description = "This endpoint is used to get the user with detail")
    @ApiResponse(responseCode = "200", description = "Return the user with detail")
    @ExtractUser(fieldName = "currentUser")
    public ResponseEntity<UserWithDetailDto> getUserWithDetail(
            @Parameter(hidden = true) User currentUser, @PathVariable String userId) {
        return new ResponseEntity<>(
                userService.getUserWithDetail(currentUser.getId(), userId), HttpStatus.OK);
    }

    @PostMapping("/follow/{followeeId}")
    @Operation(
            tags = "User",
            summary = "Follow User",
            description = "This endpoint is used to follow a user")
    @ApiResponse(responseCode = "200", description = "User followed successfully")
    @ExtractUser(fieldName = "currentUser")
    public ResponseEntity<Void> follow(
            @Parameter(hidden = true) User currentUser, @PathVariable String followeeId) {
        userService.follow(currentUser.getId(), followeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unfollow/{followeeId}")
    @Operation(
            tags = "User",
            summary = "Unfollow User",
            description = "This endpoint is used to unfollow a user")
    @ApiResponse(responseCode = "200", description = "User unfollowed successfully")
    @ExtractUser(fieldName = "currentUser")
    public ResponseEntity<Void> unfollow(
            @Parameter(hidden = true) User currentUser, @PathVariable String followeeId) {
        userService.unfollow(currentUser.getId(), followeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}/followers")
    @Operation(
            tags = "User",
            summary = "Get Followers",
            description = "This endpoint is used to get the followers of a user")
    @ApiResponse(responseCode = "200", description = "Return the followers of a user")
    public ResponseEntity<List<UserDto>> getFollowers(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(
                            required = false,
                            defaultValue = "${app-config.pagination-config.default-page-size}")
                    Integer pageSize) {
        return new ResponseEntity<>(
                userService.getFollowers(userId, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("{userId}/following")
    @Operation(
            tags = "User",
            summary = "Get Following",
            description = "This endpoint is used to get the following of a user")
    @ApiResponse(responseCode = "200", description = "Return the following of a user")
    public ResponseEntity<List<UserDto>> getFollowing(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(
                            required = false,
                            defaultValue = "${app-config.pagination-config.default-page-size}")
                    Integer pageSize) {
        return new ResponseEntity<>(
                userService.getFollowing(userId, pageNumber, pageSize), HttpStatus.OK);
    }
}
