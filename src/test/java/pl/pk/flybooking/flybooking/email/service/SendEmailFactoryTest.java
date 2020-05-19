package pl.pk.flybooking.flybooking.email.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class SendEmailFactoryTest {

    @Autowired
    private SendEmailFactory emailFactory;

    @Test
    public void findConfirmationAccountStrategy() {
        SendEmailStrategy emailStrategy = emailFactory.findEmailStrategy(StrategyName.CONFIRM_ACCOUNT);

        assertEquals(StrategyName.CONFIRM_ACCOUNT, emailStrategy.getStrategyName());
        assertTrue(emailStrategy instanceof ConfirmationAccountStrategy);
    }

    @Test
    public void findResetPasswordStrategy() {
        SendEmailStrategy emailStrategy = emailFactory.findEmailStrategy(StrategyName.RESET_PASSWORD);

        assertEquals(StrategyName.RESET_PASSWORD, emailStrategy.getStrategyName());
        assertTrue(emailStrategy instanceof ResetPasswordStrategy);
    }
}