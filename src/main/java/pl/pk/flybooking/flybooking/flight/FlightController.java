package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/flights")
public class FlightController {
    private FlightService flightService;

    @GetMapping
    ResponseEntity<List<Flight>> getMatchingFlights() throws ParseException {
        return null;
    }



}
