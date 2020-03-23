package pl.pk.flybooking.flybooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfiguration {
    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            Locale.ENGLISH,
            new Locale("pl")
    );

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(SUPPORTED_LOCALES);
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
