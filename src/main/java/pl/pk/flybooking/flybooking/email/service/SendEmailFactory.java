package pl.pk.flybooking.flybooking.email.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SendEmailFactory {

    private final Map<StrategyName, SendEmailStrategy> strategies;

    public SendEmailFactory(Set<SendEmailStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(SendEmailStrategy::getStrategyName, sendEmailStrategy -> sendEmailStrategy));
    }

    public SendEmailStrategy findEmailStrategy(StrategyName strategyName) {
        return strategies.get(strategyName);
    }
}
