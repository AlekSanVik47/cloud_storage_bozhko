package controllers;

import api.AuthApi;
import dto.request.AuthRequest;
import dto.request.RegistrationRequest;
import dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> login(AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegistrationRequest request) {
        return null;
    }
}
