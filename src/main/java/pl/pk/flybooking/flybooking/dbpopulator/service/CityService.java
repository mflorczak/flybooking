package pl.pk.flybooking.flybooking.dbpopulator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.dbpopulator.model.City;
import pl.pk.flybooking.flybooking.dbpopulator.repository.CityRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CityService {

    private CityRepository repository;

    public List<City> findCities(String name) {
        return repository.findCitiesByName(name);
    }
}
