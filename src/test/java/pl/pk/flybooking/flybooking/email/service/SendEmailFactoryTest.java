package pl.pk.flybooking.flybooking.email.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SendEmailFactoryTest {

    @Autowired
    private SendEmailFactory emailFactory;

    @Test
    void findConfirmationAccountStrategy() {
        SendEmailStrategy emailStrategy = emailFactory.findEmailStrategy(StrategyName.CONFIRM_ACCOUNT);

        assertEquals(StrategyName.CONFIRM_ACCOUNT, emailStrategy.getStrategyName());
        assertTrue(emailStrategy instanceof ConfirmationAccountStrategy);
    }

    @Test
    void findResetPasswordStrategy() {
        SendEmailStrategy emailStrategy = emailFactory.findEmailStrategy(StrategyName.RESET_PASSWORD);

        assertEquals(StrategyName.RESET_PASSWORD, emailStrategy.getStrategyName());
        assertTrue(emailStrategy instanceof ResetPasswordStrategy);
    }
}