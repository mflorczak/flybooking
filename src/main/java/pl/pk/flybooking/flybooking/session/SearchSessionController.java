package pl.pk.flybooking.flybooking.session;

import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightService;
import pl.pk.flybooking.flybooking.placesdb.model.PlaceReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "/session")
@AllArgsConstructor
public class SearchSessionController {

    private SearchSessionService searchSessionService;
    private FlightService flightService;

    private PlaceReader placeReader;

    @PostMapping("/live")
    public ResponseEntity<List<Flight>> createSession(@RequestParam String originPlaceId, @RequestParam String destinationPlaceId,
                                                      @RequestParam String outboundDate, @RequestParam String inboundDate) throws UnirestException, IOException, ParseException {
        searchSessionService.getJsonDataFromSession(searchSessionService.getSessionKey(originPlaceId, destinationPlaceId,outboundDate, inboundDate), originPlaceId, destinationPlaceId);
        return ResponseEntity.ok(flightService.getFlightsByPlaceIds(searchSessionService.getAirportIds(originPlaceId, destinationPlaceId)));
        //return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/file")
    public ResponseEntity<List<Flight>> get() throws IOException, ParseException {
        searchSessionService.clearDatabaseTables();
        searchSessionService.dataFromFile();

        System.out.println(searchSessionService.getAirportIds("SFOA", "LOND"));
        System.out.println(searchSessionService.getPlacesIds("SFOA", "LOND"));

        return ResponseEntity.ok(flightService.getFlightsByPlaceIds(searchSessionService.getAirportIds("SFOA", "LOND")));
        //return ResponseEntity.ok(flightService.getAllFlights());
    }
    //String originPlaceId, @RequestParam String destinationPlaceId,
    //                                @RequestParam String outboundDate, @RequestParam String inboundDAte


//    @GetMapping("/db")
//    public void db(){
//        placeReader
//    }
}
