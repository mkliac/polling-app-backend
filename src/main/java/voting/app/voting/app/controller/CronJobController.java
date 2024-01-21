package voting.app.voting.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

//dummy controller for cronjob to activate server hosted on render
@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin
public class CronJobController {
    @GetMapping
    public ResponseEntity<String> dummy() {
        log.info("CronJob triggered at {}", LocalDateTime.now());
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
