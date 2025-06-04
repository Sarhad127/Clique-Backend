package org.tutorial.clique.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tutorial.clique.dto.LoginUserDto;
import org.tutorial.clique.dto.RegisterUserDto;
import org.tutorial.clique.model.User;
import org.tutorial.clique.service.AuthenticationService;
import org.tutorial.clique.service.JwtService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        logger.info("Received login payload: {}", loginUserDto);
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("token", jwtToken);
            return ResponseEntity.ok().body(tokenResponse);

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "INVALID_CREDENTIALS");
            return ResponseEntity.status(400).body(errorResponse);
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
