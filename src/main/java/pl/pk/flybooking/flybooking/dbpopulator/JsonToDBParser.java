package pl.pk.flybooking.flybooking.dbpopulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.dbpopulator.model.Airport;
import pl.pk.flybooking.flybooking.dbpopulator.model.City;
import pl.pk.flybooking.flybooking.dbpopulator.model.Continent;
import pl.pk.flybooking.flybooking.dbpopulator.model.Country;
import pl.pk.flybooking.flybooking.dbpopulator.repository.AirportRepository;
import pl.pk.flybooking.flybooking.dbpopulator.repository.CityRepository;
import pl.pk.flybooking.flybooking.dbpopulator.repository.ContinentRepository;
import pl.pk.flybooking.flybooking.dbpopulator.repository.CountryRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class JsonToDBParser {
    final ObjectMapper objectMapper = new ObjectMapper();

    private ContinentRepository continentRepository;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private AirportRepository airportRepository;

    public void populateDB() throws IOException {

        String content = new String(Files.readAllBytes(Paths.get("places.json")));
        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(content);

        jsonNode.get("Continents").forEach(n -> {

            Continent continent = new Continent();
            continent.setId(n.get("Id").toPrettyString().replace("\"", ""));
            continent.setName(n.get("Name").toPrettyString().replace("\"", ""));
            continentRepository.save(continent);

            n.get("Countries").forEach(co -> {
                Country country = new Country();
                country.setCurrencyId(co.get("CurrencyId").toPrettyString().replace("\"", ""));
                country.setId(co.get("Id").toPrettyString().replace("\"", ""));
                country.setName(co.get("Name").toPrettyString().replace("\"", ""));
                country.setContinent(continent);
                countryRepository.save(country);
                co.get("Cities").forEach(ci -> {

                    City city = new City();
                    city.setId(ci.get("Id").toPrettyString().replace("\"", ""));
                    city.setCountry(country);
                    city.setIATACode(ci.get("IataCode").toPrettyString().replace("\"", ""));
                    city.setLocation(ci.get("Location").toPrettyString().replace("\"", ""));
                    city.setName(ci.get("Name").toPrettyString().replace("\"", ""));
                    cityRepository.save(city);
                    ci.get("Airports").forEach(a -> {

                        Airport airport = new Airport();
                        airport.setId(a.get("Id").toPrettyString().replace("\"", ""));
                        airport.setName(a.get("Name").toPrettyString().replace("\"", ""));
                        airport.setCountry(country);
                        airport.setCity(city);
                        airport.setLocation(a.get("Location").toPrettyString().replace("\"", ""));
                        airportRepository.save(airport);
                    });

                });

            });

        });
    }

}
