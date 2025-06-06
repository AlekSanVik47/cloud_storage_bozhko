package services;

import dto.request.AuthRequest;
import dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;


    public AuthResponse authenticate(AuthRequest authRequest) {
Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(authRequest.getEmail());

        AuthResponse authResponse= AuthResponse.builder()
                .username(userDetails.getUsername())
                .build();

        return authResponse;


    }
}
