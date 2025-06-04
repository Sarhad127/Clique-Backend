package org.tutorial.clique.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tutorial.clique.dto.LoginUserDto;
import org.tutorial.clique.dto.RegisterUserDto;
import org.tutorial.clique.model.User;
import org.tutorial.clique.service.AuthenticationService;
import org.tutorial.clique.service.JwtService;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public LoginController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            String authHeader = "Bearer " + jwtToken;

            return ResponseEntity.ok().body(Collections.singletonMap("token", jwtToken));

        } catch (DisabledException e) {
            try {
                return ResponseEntity.status(400).body(
                        Collections.singletonMap("error", "UNVERIFIED_USER")
                );
            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Failed to resend verification code. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid credentials");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            authenticationService.signup(registerUserDto);
            return ResponseEntity.ok().body("Registration successful! Please check your email to verify your account.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Registration failed: " + e.getMessage());
        }
    }
}
