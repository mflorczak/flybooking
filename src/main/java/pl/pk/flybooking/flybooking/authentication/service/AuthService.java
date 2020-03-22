package pl.pk.flybooking.flybooking.authentication.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.confirmation.model.ConfirmationToken;
import pl.pk.flybooking.flybooking.confirmation.repository.ConfirmationTokenRepository;
import pl.pk.flybooking.flybooking.email.EmailSenderService;
import pl.pk.flybooking.flybooking.exception.AppException;
import pl.pk.flybooking.flybooking.payload.ApiResponse;
import pl.pk.flybooking.flybooking.payload.JwtAuthenticationResponse;
import pl.pk.flybooking.flybooking.payload.LoginRequest;
import pl.pk.flybooking.flybooking.role.model.Role;
import pl.pk.flybooking.flybooking.role.model.RoleName;
import pl.pk.flybooking.flybooking.role.repository.RoleRepository;
import pl.pk.flybooking.flybooking.security.service.JwtTokenProvider;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private PasswordEncoder passwordEncoder;
    private EmailSenderService senderService;
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;

    @Transactional
    public ApiResponse registrationInactiveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        user.setEnabled(false);

        userRepository.save(user);

        sendMailWithTokenToUser(user, "Complete Registration!", "To confirm your account, please click here : ", "confirm-account");

        return new ApiResponse(true, "User registered successfully. Please check your email address and finish registration process");
    }

    public ApiResponse forgotUserPassword(User user) {
        sendMailWithTokenToUser(user, "Complete Password Reset!","To complete the password reset process, please click here: ", "confirm-reset");
        return new  ApiResponse(true , "Request to reset password received. Check your inbox for the reset link.");
    }

    private void sendMailWithTokenToUser(User user, String subject, String text, String action) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(text + "http://localhost:8080/api/auth/" + action + "?token="+confirmationToken.getToken());

        senderService.sendEmail(mailMessage);
    }

    public JwtAuthenticationResponse generateResponse(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    public ApiResponse resetPassword(User user) {
        User tokenUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User email not found: " + user.getEmail()));

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        tokenUser.setPassword(encodedPassword);
        userRepository.save(tokenUser);
        return new ApiResponse(true, "Password successfully reset. You can now log in with the new credentials.");
    }
}
