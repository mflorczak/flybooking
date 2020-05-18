package pl.pk.flybooking.flybooking.email.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.confirmation.model.ConfirmationToken;
import pl.pk.flybooking.flybooking.confirmation.repository.ConfirmationTokenRepository;
import pl.pk.flybooking.flybooking.email.EmailSenderService;
import pl.pk.flybooking.flybooking.payload.service.MessageTranslateService;
import pl.pk.flybooking.flybooking.user.model.User;

import java.util.Locale;

@Service
@AllArgsConstructor
public class ConfirmationAccountStrategy implements SendEmailStrategy {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService senderService;
    private final MessageTranslateService messageTranslateService;

    @Override
    public void sendEmail(User user, Locale locale) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        String subject = messageTranslateService.translatedMessage("completeRegistration", locale);
        String text = messageTranslateService.translatedMessage("confirmAccount", locale);
        text += "http://localhost:8080/api/auth/confirm-account?token=" + confirmationToken.getToken();

        senderService.sendEmail(prepareInformationToSend(user.getEmail(), subject, text));
    }

    @Override
    public StrategyName getStrategyName() {
        return StrategyName.CONFIRM_ACCOUNT;
    }
}
