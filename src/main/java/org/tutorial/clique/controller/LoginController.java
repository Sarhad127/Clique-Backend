package org.tutorial.clique.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public LoginController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("token", jwtToken);
            return ResponseEntity.ok(tokenResponse);

        } catch (UsernameNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "EMAIL_NOT_FOUND");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "WRONG_PASSWORD");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (DisabledException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "ACCOUNT_NOT_VERIFIED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "INVALID_CREDENTIALS");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
