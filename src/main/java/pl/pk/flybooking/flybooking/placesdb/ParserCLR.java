package pl.pk.flybooking.flybooking.placesdb;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.session.SearchSessionService;

@Component
@AllArgsConstructor
public class ParserCLR implements CommandLineRunner {

    private JsonToDBParser jsonToDBParser;
    private SearchSessionService searchSessionService;

    @Override
    public void run(String... args) throws Exception {
            //jsonToDBParser.populateDB();
    }
}
