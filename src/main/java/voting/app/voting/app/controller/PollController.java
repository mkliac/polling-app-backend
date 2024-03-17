package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.app.voting.app.aop.ExtractUser;
import voting.app.voting.app.dto.AddPollItemsRequest;
import voting.app.voting.app.dto.DeletePollItemsRequest;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.User;
import voting.app.voting.app.service.PollService;

import java.util.List;

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
    @ApiResponse(
            responseCode = "200",
            description = "Return the poll by id")
    public ResponseEntity<Poll> getPoll(@PathVariable String id) {
        return new ResponseEntity<>(pollService.getPoll(id), HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            tags = "Poll",
            summary = "Get Polls",
            description = "This endpoint is used to get all user's polls")
    @ApiResponse(
            responseCode = "200",
            description = "Return all user's polls")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<List<Poll>> getPolls(@Parameter(hidden = true) User user) {
        return new ResponseEntity<>(pollService.getPolls(user), HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            tags = "Poll",
            summary = "Save Poll",
            description = "This endpoint is used to save a poll")
    @ApiResponse(
            responseCode = "200",
            description = "Return the saved poll")
    @ExtractUser(fieldName = "createdBy")
    public ResponseEntity<Poll> savePoll(@Parameter(hidden = true) User createdBy, @Valid @RequestBody SavePollRequest request) {
        return new ResponseEntity<>(pollService.savePoll(createdBy, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = "Poll",
            summary = "Delete Poll",
            description = "This endpoint is used to delete a poll by id")
    @ApiResponse(
            responseCode = "200",
            description = "Void")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Void> deletePoll(@Parameter(hidden = true) User user, @PathVariable String id) {
        pollService.deletePoll(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/items")
    @Operation(
            tags = "Poll",
            summary = "Add Poll Items",
            description = "This endpoint is used to add extra items to a poll")
    @ApiResponse(
            responseCode = "200",
            description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> addPollItems(@Parameter(hidden = true) User user, @PathVariable String id, @Valid @RequestBody AddPollItemsRequest request) {
        return new ResponseEntity<>(pollService.addPollItems(user, id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/items")
    @Operation(
            tags = "Poll",
            summary = "Delete Poll Items",
            description = "This endpoint is used to delete items from a poll")
    @ApiResponse(
            responseCode = "200",
            description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> deletePollItems(@Parameter(hidden = true) User user, @PathVariable String id, @Valid @RequestBody DeletePollItemsRequest request) {
        return new ResponseEntity<>(pollService.deletePollItems(user, id, request), HttpStatus.OK);
    }

    @PostMapping("/{id}/items/{itemId}")
    @Operation(
            tags = "Poll",
            summary = "Update Poll Item",
            description = "This endpoint is used to update a poll item")
    @ApiResponse(
            responseCode = "200",
            description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> updatePollItem(@Parameter(hidden = true) User user, @PathVariable String id, @PathVariable String itemId, @RequestParam String text) {
        return new ResponseEntity<>(pollService.updatePollItem(user, id, itemId, text), HttpStatus.OK);
    }

    @PostMapping("/{id}/items/{itemId}/vote")
    @Operation(
            tags = "Poll",
            summary = "Vote",
            description = "This endpoint is used to vote on a poll item")
    @ApiResponse(
            responseCode = "200",
            description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> vote(@Parameter(hidden = true) User user, @PathVariable String id, @PathVariable String itemId) {
        return new ResponseEntity<>(pollService.vote(user, id, itemId), HttpStatus.OK);
    }

    @PostMapping("/{id}/close")
    @Operation(
            tags = "Poll",
            summary = "Close Poll",
            description = "This endpoint is used to close a poll")
    @ApiResponse(
            responseCode = "200",
            description = "Return the updated poll")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> closePoll(@Parameter(hidden = true) User user, @PathVariable String id) {
        return new ResponseEntity<>(pollService.closePoll(user, id), HttpStatus.OK);
    }

    @GetMapping("/items/{id}/voters")
    @Operation(
            tags = "Poll",
            summary = "Get Voters",
            description = "This endpoint is used to get voters of a poll item")
    @ApiResponse(
            responseCode = "200",
            description = "Return the voters of a poll item")
    public ResponseEntity<List<User>> getVoters(@PathVariable String id) {
        return new ResponseEntity<>(pollService.getVoters(id), HttpStatus.OK);
    }
}
