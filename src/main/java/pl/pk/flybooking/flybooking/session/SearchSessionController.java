package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.annotation.JsonView;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pk.flybooking.flybooking.flight.Flight;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "api/search")
@AllArgsConstructor
public class SearchSessionController {

    private SearchSessionService searchSessionService;

    @PostMapping("/live")
    @JsonView(Flight.JsonViews.get.class)
    public ResponseEntity<List<Flight>> createSession(@RequestParam String originPlaceId, @RequestParam String destinationPlaceId,
                                                      @RequestParam String outboundDate, @RequestParam String inboundDate) throws UnirestException, IOException, ParseException {
        return ResponseEntity.ok(searchSessionService.getResults(searchSessionService.getSessionKey(originPlaceId, destinationPlaceId,outboundDate, inboundDate), originPlaceId, destinationPlaceId));

    }

    @GetMapping("/file")
    @JsonView(Flight.JsonViews.get.class)
    public ResponseEntity<List<Flight>> get(@RequestParam String originPlaceId, @RequestParam String destinationPlaceId,
                                            @RequestParam String outboundDate, @RequestParam String inboundDate) throws IOException {
        return ResponseEntity.ok(searchSessionService.fromFile(originPlaceId, destinationPlaceId));
    }

}
