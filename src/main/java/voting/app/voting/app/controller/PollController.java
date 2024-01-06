package voting.app.voting.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.service.PollService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {
    private final PollService pollService;

    //TODO: expose all required API endpoints

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPoll(@PathVariable String id) {
        return new ResponseEntity<>(pollService.getPoll(id), HttpStatus.OK);
    }

    //TODO: update savePoll endpoint

    @PostMapping
    public ResponseEntity<Poll> savePoll() {
        return new ResponseEntity<>(pollService.savePoll(), HttpStatus.OK);
    }
}
