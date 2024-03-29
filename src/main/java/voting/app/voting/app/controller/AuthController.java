package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import voting.app.voting.app.dto.google.AuthResponse;
import voting.app.voting.app.service.GoogleAuthService;

@RestController
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final GoogleAuthService googleAuthService;

    @PostMapping
    @Operation(
            tags = "Auth",
            summary = "Get Tokens",
            description = "This endpoint is used to get tokens")
    @ApiResponse(responseCode = "200", description = "Return tokens")
    public ResponseEntity<AuthResponse> getTokens(
            @RequestParam String code, @RequestParam String redirectUri) {
        return new ResponseEntity<>(googleAuthService.getTokens(code, redirectUri), HttpStatus.OK);
    }

    @GetMapping("/refresh-token")
    @Operation(
            tags = "Auth",
            summary = "Refresh Token",
            description = "This endpoint is used to refresh token")
    @ApiResponse(responseCode = "200", description = "Return refreshed token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        return new ResponseEntity<>(googleAuthService.refreshToken(refreshToken), HttpStatus.OK);
    }

    @PostMapping("/revoke-token")
    @Operation(
            tags = "Auth",
            summary = "Revoke Token",
            description = "This endpoint is used to revoke token")
    @ApiResponse(responseCode = "200", description = "Token revoked")
    public ResponseEntity<Void> revokeToken(@RequestParam String token) {
        googleAuthService.revokeToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
