package pl.pk.flybooking.flybooking.authentication.service;

import lombok.AllArgsConstructor;
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
import pl.pk.flybooking.flybooking.exception.GenericValidationException;
import pl.pk.flybooking.flybooking.payload.ApiResponse;
import pl.pk.flybooking.flybooking.payload.JwtAuthenticationResponse;
import pl.pk.flybooking.flybooking.payload.LoginRequest;
import pl.pk.flybooking.flybooking.payload.service.MessageTranslateService;
import pl.pk.flybooking.flybooking.role.model.Role;
import pl.pk.flybooking.flybooking.role.model.RoleName;
import pl.pk.flybooking.flybooking.role.repository.RoleRepository;
import pl.pk.flybooking.flybooking.security.service.JwtTokenProvider;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService senderService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MessageTranslateService messageTranslateService;

    @Transactional
    public ApiResponse registrationInactiveUser(User user, Locale locale) {

        if (userRepository.existsByUsername(user.getUsername()))
            throw new GenericValidationException("usernameIsAlreadyTaken", user.getUsername());

        if (userRepository.existsByEmail(user.getEmail()))
            throw new GenericValidationException("emailAlreadyInUse", user.getEmail());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new GenericValidationException("userRoleNotSet", RoleName.ROLE_USER.toString()));

        user.setRoles(Collections.singleton(userRole));
        user.setEnabled(false);

        userRepository.save(user);
        String subject = messageTranslateService.translatedMessage("completeRegistration", locale);
        String text = messageTranslateService.translatedMessage("confirmAccount", locale);

        sendMailWithTokenToUser(user, subject, text, "confirm-account");
        String responseMessage = messageTranslateService.translatedMessage("userRegisteredSuccessfully", locale);

        return new ApiResponse(true, responseMessage);
    }

    public ApiResponse forgotUserPassword(User user, Locale locale) {
        User existingUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername())
                .orElseThrow(() -> new GenericValidationException("userNotFound", user.getUsername()));

        String subject = messageTranslateService.translatedMessage("completePasswordReset", locale);
        String text = messageTranslateService.translatedMessage("completePasswordResetProcess", locale);
        String responseMessage = messageTranslateService.translatedMessage("requestResetPassword", locale);
        sendMailWithTokenToUser(existingUser, subject,text, "confirm-reset");
        return new ApiResponse(true , responseMessage);
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
    public ApiResponse resetPassword(User user, Locale locale) {
        User tokenUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new GenericValidationException("userEmailNotFound", user.getEmail()));

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        tokenUser.setPassword(encodedPassword);
        userRepository.save(tokenUser);

        String responseMessage = messageTranslateService.translatedMessage("passwordSuccessfullyReset", locale);
        return new ApiResponse(true, responseMessage);
    }
}
