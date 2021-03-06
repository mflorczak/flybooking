package pl.pk.flybooking.flybooking.authentication.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.email.service.SendEmailFactory;
import pl.pk.flybooking.flybooking.email.service.StrategyName;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MessageTranslateService messageTranslateService;
    private final SendEmailFactory sendEmailFactory;

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
        sendEmailFactory.findEmailStrategy(StrategyName.CONFIRM_ACCOUNT).sendEmail(user, locale);
        String responseMessage = messageTranslateService.translatedMessage("userRegisteredSuccessfully", locale);

        return new ApiResponse(true, responseMessage);
    }

    @Transactional
    public ApiResponse forgotUserPassword(User user, Locale locale) {
        User existingUser = findUserByUsernameOrEmail(user.getUsername(), user.getUsername());

        sendEmailFactory.findEmailStrategy(StrategyName.RESET_PASSWORD).sendEmail(existingUser, locale);

        String responseMessage = messageTranslateService.translatedMessage("requestResetPassword", locale);
        return new ApiResponse(true , responseMessage);
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
        User user = findUserByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail());
        JwtAuthenticationResponse authenticationResponse = new JwtAuthenticationResponse(jwt);
        authenticationResponse.setEmailAddress(user.getEmail());
        authenticationResponse.setUsername(user.getUsername());
        return authenticationResponse;
    }

    @Transactional
    public ApiResponse resetPassword(User user, Locale locale) {
        User tokenUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new GenericValidationException("userEmailNotFound", user.getEmail()));

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        tokenUser.setPassword(encodedPassword);

        String responseMessage = messageTranslateService.translatedMessage("passwordSuccessfullyReset", locale);
        return new ApiResponse(true, responseMessage);
    }

    public User findUserByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new GenericValidationException("userNotFound", username));
    }
}
