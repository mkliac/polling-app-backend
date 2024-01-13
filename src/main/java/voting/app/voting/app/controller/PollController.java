package voting.app.voting.app.controller;

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
public class PollController {
    private final PollService pollService;

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable String id) {
        return new ResponseEntity<>(pollService.getPoll(id), HttpStatus.OK);
    }

    @GetMapping
    @ExtractUser(fieldName = "user")
    public ResponseEntity<List<Poll>> getPolls(User user) {
        return new ResponseEntity<>(pollService.getPolls(user), HttpStatus.OK);
    }

    @PostMapping
    @ExtractUser(fieldName = "createdBy")
    public ResponseEntity<Poll> savePoll(User createdBy,@Valid @RequestBody SavePollRequest request) {
        return new ResponseEntity<>(pollService.savePoll(createdBy, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Void> deletePoll(User user, @PathVariable String id) {
        pollService.deletePoll(user, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/items")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> addPollItems(User user, @PathVariable String id, @Valid @RequestBody AddPollItemsRequest request) {
        return new ResponseEntity<>(pollService.addPollItems(user, id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/items")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> deletePollItems(User user, @PathVariable String id, @Valid @RequestBody DeletePollItemsRequest request) {
        return new ResponseEntity<>(pollService.deletePollItems(user, id, request), HttpStatus.OK);
    }

    @PutMapping("/{id}/items/{itemId}")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> updatePollItem(User user, @PathVariable String id, @PathVariable String itemId, @RequestParam String text) {
        return new ResponseEntity<>(pollService.updatePollItem(user, id, itemId, text), HttpStatus.OK);
    }

    @PutMapping("/{id}/items/{itemId}/vote")
    @ExtractUser(fieldName = "user")
    public ResponseEntity<Poll> vote(User user, @PathVariable String id, @PathVariable String itemId) {
        return new ResponseEntity<>(pollService.vote(user, id, itemId), HttpStatus.OK);
    }
}
