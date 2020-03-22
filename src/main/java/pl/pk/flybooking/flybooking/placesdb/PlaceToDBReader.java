package pl.pk.flybooking.flybooking.placesdb;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.placesdb.model.PlaceReader;

@Component
@AllArgsConstructor
public class PlaceToDBReader implements CommandLineRunner {

    private PlaceReader placeReader;

    @Override
    public void run(String... args) throws Exception {
//        placeReader.populateDB();
    }
}
