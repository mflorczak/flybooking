package pl.pk.flybooking.flybooking.email.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.email.EmailSenderService;
import pl.pk.flybooking.flybooking.payload.service.MessageTranslateService;
import pl.pk.flybooking.flybooking.user.model.User;

import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResetPasswordStrategy implements SendEmailStrategy {

    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService senderService;
    private final MessageTranslateService messageTranslateService;

    @Override
    public void sendEmail(User user, Locale locale) {
        final String newPassword = UUID.randomUUID().toString().substring(0, 10);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        String subject = messageTranslateService.translatedMessage("completePasswordReset", locale);
        String text = messageTranslateService.translatedMessage("completePasswordResetProcess", locale);
        text += newPassword;

        senderService.sendEmail(prepareInformationToSend(user.getEmail(), subject, text));
    }

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.RESET_PASSWORD;
    }
}
