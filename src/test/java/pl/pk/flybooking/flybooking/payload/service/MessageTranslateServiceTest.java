package pl.pk.flybooking.flybooking.payload.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageTranslateServiceTest {

    @Autowired
    private MessageTranslateService messageTranslateService;

    @Test
    public void testGetMessageForPolishLanguage() {
        Locale pl = new Locale("pl");
        String message = messageTranslateService.translatedMessage("verificationFinishedSuccessfully", pl);
        assertThat(message).isEqualTo("Weryfikacja zakoñczona sukcesem.");
    }

    @Test
    public void testGetMessageForEnglishLanguage() {
        String message = messageTranslateService.translatedMessage("verificationFinishedSuccessfully", Locale.ENGLISH);
        assertThat(message).isEqualTo("Verification finished successfully.");
    }

    @Test
    public void whenMessageCodeNotExist_thenReturn_messageAsCode() {
        String message = messageTranslateService.translatedMessage("incorrectCode", Locale.ENGLISH);
        assertThat(message).isEqualTo("incorrectCode");

        String message2 = messageTranslateService.translatedMessage("I don't know that we have internationalization.", Locale.ENGLISH);
        assertThat(message2).isEqualTo("I don't know that we have internationalization.");
    }
}
