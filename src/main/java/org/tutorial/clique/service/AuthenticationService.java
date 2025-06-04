package org.tutorial.clique.service;

import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutorial.clique.configuration.MyUserDetailService;
import org.tutorial.clique.dto.LoginUserDto;
import org.tutorial.clique.dto.RegisterUserDto;
import org.tutorial.clique.exceptions.DuplicateUserException;
import org.tutorial.clique.model.User;
import org.tutorial.clique.repository.UserRepository;

import java.util.Random;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final MyUserDetailService myUserDetailService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService,
                                 MyUserDetailService myUserDetailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.myUserDetailService = myUserDetailService;
    }

    public void signup(RegisterUserDto input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new DuplicateUserException("Email is already taken.");
        }

        if (userRepository.existsByUsername(input.getUsername())) {
            throw new DuplicateUserException("Username is already taken.");
        }
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = (User) myUserDetailService.loadUserByUsername(input.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (DisabledException ex) {
            throw new DisabledException("Account not verified");
        }

        return user;
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String htmlMessage = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Welcome</title>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f9fafb;'>" +
                "<div style='max-width: 600px; margin: 40px auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);'>" +
                "<div style='text-align: center;'>" +
                "<h2 style='color: #2563eb;'>Thank you for signing up to Clique</h2>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}