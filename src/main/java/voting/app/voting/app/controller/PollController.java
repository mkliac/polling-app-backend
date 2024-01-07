package voting.app.voting.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.app.voting.app.aop.ExtractUser;
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
    public ResponseEntity<Poll> savePoll(User createdBy, @RequestBody SavePollRequest request) {
        return new ResponseEntity<>(pollService.savePoll(createdBy, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable String id) {
        pollService.deletePoll(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Poll> addPollItems(@PathVariable String id, @RequestBody List<String> itemTexts) {
        return new ResponseEntity<>(pollService.addPollItems(id, itemTexts), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<Poll> deletePollItems(@PathVariable String id, @RequestBody List<String> itemIds) {
        return new ResponseEntity<>(pollService.deletePollItems(id, itemIds), HttpStatus.OK);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<Poll> updatePollItem(@PathVariable String id, @PathVariable String itemId, @RequestParam String text) {
        return new ResponseEntity<>(pollService.updatePollItem(id, itemId, text), HttpStatus.OK);
    }
}
