package pl.pk.flybooking.flybooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FlybookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlybookingApplication.class, args);
	}

}
