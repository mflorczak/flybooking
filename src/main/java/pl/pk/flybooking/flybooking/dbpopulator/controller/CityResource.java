package pl.pk.flybooking.flybooking.dbpopulator.controller;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.pk.flybooking.flybooking.dbpopulator.model.City;
import pl.pk.flybooking.flybooking.dbpopulator.service.CityService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CityResource {

    private final CityService cityService;

    @GetMapping("/cities")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(City.JsonViews.get.class)
    public List<City> searchCitiesByName(@RequestParam String cityName) {
        return cityService.findCities(cityName.toLowerCase());
    }
}
