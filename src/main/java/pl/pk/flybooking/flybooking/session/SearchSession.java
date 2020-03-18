package pl.pk.flybooking.flybooking.session;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "/session")
@AllArgsConstructor
public class SearchSession {

    private SearchSessionService searchSessionService;
    private FlightService flightService;

    @PostMapping("/live")
    public ResponseEntity<List<Flight>> createSession() throws UnirestException, IOException, ParseException {
        searchSessionService.getJsonDataFromSession(searchSessionService.getSessionKey());
        return ResponseEntity.ok(flightService.getFlightsByPlaces(16216L, 13554L));
        //return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/file")
    public ResponseEntity<List<Flight>> get() throws IOException, ParseException {
        searchSessionService.clearDatabaseTables();
        searchSessionService.dataFromFile();
        return ResponseEntity.ok(flightService.getFlightsByPlaces(13554L,16216L ));
        //return ResponseEntity.ok(flightService.getAllFlights());

    }
}
