package polling.app.polling.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polling.app.polling.app.aop.ExtractUser;
import polling.app.polling.app.dto.poll.PollDto;
import polling.app.polling.app.dto.poll.PollFilterType;
import polling.app.polling.app.dto.poll.SavePollRequest;
import polling.app.polling.app.dto.poll.UpdatePollRequest;
import polling.app.polling.app.dto.user.UserDto;
import polling.app.polling.app.model.user.User;
import polling.app.polling.app.service.PollService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
@CrossOrigin
@SecurityRequirement(name = "BEARER TOKEN")
public class PollController {
    private final PollService pollService;

    @GetMapping("/{id}")
    @Operation(
            tags = "Poll",
            summary = "Get Poll",
            description = "This endpoint is used to get a poll by id")
    @ApiResponse(responseCode = "200", description = "Return the poll by id")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<PollDto> getPoll(
            @Parameter(hidden = true) User user, @PathVariable String id) {
        return new ResponseEntity<>(pollService.getPoll(user, id), HttpStatus.OK);
    }

    @GetMapping(value = {"", "/users/{userId}"})
    @Operation(
            tags = "Poll",
            summary = "Get Polls",
            description = "This endpoint is used to get all user's polls")
    @ApiResponse(responseCode = "200", description = "Return all user's polls")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<List<PollDto>> getPolls(
            @Parameter(hidden = true) User user,
            @PathVariable(required = false) String userId,
            @RequestParam(required = false, defaultValue = "PUBLIC") PollFilterType filterType,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "false") boolean isAscending,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(
                            required = false,
                            defaultValue = "${app-config.pagination-config.default-page-size}")
                    Integer pageSize) {
        return new ResponseEntity<>(
                pollService.getPolls(
                        user,
                        userId,
                        filterType,
                        search,
                        isAscending,
                        sortBy,
                        pageNumber,
                        pageSize),
                HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            tags = "Poll",
            summary = "Save Poll",
            description = "This endpoint is used to save a poll")
    @ApiResponse(responseCode = "200", description = "Return the saved poll")
    @ExtractUser(fieldName = "createdBy")
    public ResponseEntity<PollDto> savePoll(
            @Parameter(hidden = true) User createdBy, @Valid @RequestBody SavePollRequest request) {
        return new ResponseEntity<>(pollService.savePoll(createdBy, request), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @Operation(
            tags = "Poll",
            summary = "Update Poll",
            description = "This endpoint is used to update a poll by id")
    @ApiResponse(responseCode = "200", description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<PollDto> updatePoll(
            @Parameter(hidden = true) User user,
            @PathVariable String id,
            @Valid @RequestBody UpdatePollRequest request) {
        return new ResponseEntity<>(pollService.updatePoll(user, id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = "Poll",
            summary = "Delete Poll",
            description = "This endpoint is used to delete a poll by id")
    @ApiResponse(responseCode = "200", description = "Void")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Void> deletePoll(
            @Parameter(hidden = true) User user, @PathVariable String id) {
        pollService.deletePoll(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/close")
    @Operation(
            tags = "Poll",
            summary = "Close Poll",
            description = "This endpoint is used to close a poll")
    @ApiResponse(responseCode = "200", description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<PollDto> closePoll(
            @Parameter(hidden = true) User user, @PathVariable String id) {
        return new ResponseEntity<>(pollService.closePoll(user, id), HttpStatus.OK);
    }

    @PostMapping("/{id}/items/{itemId}/vote")
    @Operation(
            tags = "Poll",
            summary = "Vote",
            description = "This endpoint is used to vote on a poll item")
    @ApiResponse(responseCode = "200", description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<PollDto> vote(
            @Parameter(hidden = true) User user,
            @PathVariable String id,
            @PathVariable String itemId) {
        return new ResponseEntity<>(pollService.vote(user, id, itemId), HttpStatus.OK);
    }

    @GetMapping("{id}/items/{itemId}/voters")
    @Operation(
            tags = "Poll",
            summary = "Get Voters",
            description = "This endpoint is used to get voters of a poll item")
    @ApiResponse(responseCode = "200", description = "Return the voters of a poll item")
    public ResponseEntity<List<UserDto>> getVoters(
            @PathVariable String id,
            @PathVariable String itemId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(
                            required = false,
                            defaultValue = "${app-config.pagination-config.default-page-size}")
                    Integer pageSize) {
        return new ResponseEntity<>(
                pollService.getVoters(id, itemId, pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("/{id}/bookmark")
    @Operation(
            tags = "Poll",
            summary = "Bookmark Poll",
            description = "This endpoint is used to bookmark a poll")
    @ApiResponse(responseCode = "200", description = "Void")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Void> bookmarkPoll(
            @Parameter(hidden = true) User user,
            @PathVariable String id,
            @RequestParam boolean isBookmark) {
        pollService.bookmarkPoll(user, id, isBookmark);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
