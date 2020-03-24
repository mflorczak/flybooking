package pl.pk.flybooking.flybooking.authentication.controller;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pk.flybooking.flybooking.authentication.service.AuthService;
import pl.pk.flybooking.flybooking.confirmation.model.ConfirmationToken;
import pl.pk.flybooking.flybooking.confirmation.repository.ConfirmationTokenRepository;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;
import pl.pk.flybooking.flybooking.payload.ApiResponse;
import pl.pk.flybooking.flybooking.payload.JwtAuthenticationResponse;
import pl.pk.flybooking.flybooking.payload.LoginRequest;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserRepository userRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private AuthService authService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.generateResponse(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@JsonView(User.UserViews.SignUp.class) @Valid @RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already taken!"));

        if (userRepository.existsByEmail(user.getEmail()))
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email Address already in use!"));

        return ResponseEntity.ok(authService.registrationInactiveUser(user));
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<ApiResponse> confirmUserAccount(@RequestParam String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new GenericValidationException("invalidLink"));

        String username = confirmationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GenericValidationException("userNotFound", username));

        user.setEnabled(true);
        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/auth/redirect")
                .build().toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "Verification finished successfully."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotUserPassword(@JsonView(User.UserViews.ForgotPassword.class) @RequestBody User user) {
        User existingUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new GenericValidationException("userNotFound", user.getUsername()));

        return ResponseEntity.ok(authService.forgotUserPassword(existingUser));
    }

    @GetMapping("/confirm-reset")
    public ResponseEntity<ApiResponse> validateResetToken(@RequestParam String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new GenericValidationException("invalidLink"));
        User user = userRepository.findById(confirmationToken.getUser().getId())
                .orElseThrow(() -> new GenericValidationException("userNotFound", confirmationToken.getUser().getUsername()));
        return ResponseEntity.ok(new ApiResponse(true, "Verification user finished successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetUserPassword(@JsonView(User.UserViews.ResetPassword.class) @RequestBody User user) {
        return ResponseEntity.ok(authService.resetPassword(user));
    }

    @GetMapping("/redirect")
    public String test() {
        return "Routing dzi³a zajebiscie";
    }
}
