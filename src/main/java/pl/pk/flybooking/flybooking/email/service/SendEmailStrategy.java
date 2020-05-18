package pl.pk.flybooking.flybooking.email.service;

import org.springframework.mail.SimpleMailMessage;
import pl.pk.flybooking.flybooking.user.model.User;

import java.util.Locale;

public interface SendEmailStrategy {

    void sendEmail(User user, Locale locale);

    StrategyName getStrategyName();

    default SimpleMailMessage prepareInformationToSend(String userEmail, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        return mailMessage;
    }
}
