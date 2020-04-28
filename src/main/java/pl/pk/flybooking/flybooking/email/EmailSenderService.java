package pl.pk.flybooking.flybooking.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage mailMessage) {
        try {
            mailSender.send(mailMessage);
        } catch (MailAuthenticationException e) {
            throw new GenericValidationException("mailServerAuthError");
        }
    }
}
