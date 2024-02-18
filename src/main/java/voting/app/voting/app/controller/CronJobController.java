package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin
public class CronJobController {
    @GetMapping
    @Operation(
            tags = "CronJob",
            summary = "Dummy endpoint to activate server hosted on render",
            description = "This endpoint is used to activate server hosted on render")
    @ApiResponse(
            responseCode = "200",
            description = "Success")
    public ResponseEntity<String> dummy() {
        log.info("CronJob triggered at {}", LocalDateTime.now());
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
