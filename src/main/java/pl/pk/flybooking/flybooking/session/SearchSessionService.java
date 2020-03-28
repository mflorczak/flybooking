package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightService;
import pl.pk.flybooking.flybooking.parser.Parser;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.segment.Segment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchSessionService {

    private final static String UNIREST_POST_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0";
    private final static String X_RAPIDAPI_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private final static String X_RAPIDAPI_KEY = "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d";
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final static String COUNTRY = "country";
    private final static String CURRENCY = "currency";
    private final static String LOCALE = "locale";
    private final static String ORIGIN_PLACE = "originPlace";
    private final static String DESTINATION_PLACE = "destinationPlace";
    private final static String OUTBOUND_DATE = "outboundDate";
    private final static String ADULTS = "adults";
    private final static String INBOUND_DATE = "inboundDate";
    private final static String UNIREST_POST_URL_HEADER = "X-RapidAPI-Host";
    private final static String X_RAPIDAPI_KEY_HEADER = "X-RapidAPI-Key";
    private final static String CONTENT_TYPE_HEADER = "Content-Type";
    private final static String PLACE_ID_ENDING = "-sky";
    private final static String COUNTRY_VAL = "US";
    private final static String CURRENCY_VAL = "USD";
    private final static String LOCALE_VAL = "en-US";
    private final static String RESULTS_UNIREST_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/";
    private final static String RESULTS_URL_ENDING = "?pageIndex=0&pageSize=10";
    private final static int ADULTS_NUMBER = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private FlightService flightService;

    private Parser<Place> placeParser;
    private Parser<Carrier> carrierParser;
    private Parser<Segment> segmentParser;

    public String getSessionKey(String originPlaceId, @RequestParam String destinationPlaceId,
                                @RequestParam String outboundDate, @RequestParam String inboundDAte) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.post(UNIREST_POST_URL)
                .header(UNIREST_POST_URL_HEADER, X_RAPIDAPI_HOST)
                .header(X_RAPIDAPI_KEY_HEADER, X_RAPIDAPI_KEY)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                .field(COUNTRY, COUNTRY_VAL)
                .field(CURRENCY, CURRENCY_VAL)
                .field(LOCALE, LOCALE_VAL)
                .field(ORIGIN_PLACE, originPlaceId + PLACE_ID_ENDING)
                .field(DESTINATION_PLACE, destinationPlaceId + PLACE_ID_ENDING)
                .field(OUTBOUND_DATE, outboundDate)
                .field(ADULTS, ADULTS_NUMBER)
                .field(INBOUND_DATE, inboundDAte)
                .asJson();

        String locationURL = response.getHeaders().getFirst("Location");
        System.out.println(locationURL);
        return getLocationStringFromURL(locationURL);
    }

    public List<Flight> fromFile(String originPlaceId, String destinationPlaceId) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("sample.json")));
        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(content);
        List<Flight> flights = flightService.createFlights(carrierParser.parse(jsonNode), placeParser.parse(jsonNode), segmentParser.parse(jsonNode));

        return filterFlightsByOriginAndDestination(flights, originPlaceId, destinationPlaceId);
    }

    public List<Flight> getResults(String sessionKey, String originPlaceId, String destinationPlaceId) throws UnirestException, IOException, ParseException {
        HttpResponse<JsonNode> response = Unirest.get(RESULTS_UNIREST_URL + sessionKey + RESULTS_URL_ENDING)
                .header(UNIREST_POST_URL_HEADER, X_RAPIDAPI_HOST)
                .header(X_RAPIDAPI_KEY_HEADER, X_RAPIDAPI_KEY)
                .asJson();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());
        System.out.println(jsonNode.toPrettyString());

        List<Flight> flights = flightService.createFlights(carrierParser.parse(jsonNode), placeParser.parse(jsonNode), segmentParser.parse(jsonNode));
        return filterFlightsByOriginAndDestination(flights, originPlaceId, destinationPlaceId);
    }

    private List<Flight> filterFlightsByOriginAndDestination(List<Flight> flights, String originId, String destinationId) {
        return flights.stream().filter(f -> (f.getOriginStation().getCity().getId().equals(originId) && f.getDestinationStation().getCity().getId().equals(destinationId))
                || (f.getOriginStation().getCity().getId().equals(destinationId) && f.getDestinationStation().getCity().getId().equals(originId)))
                .collect(Collectors.toList());
    }

    private static String getLocationStringFromURL(String locationURL) {
        return locationURL.substring(locationURL.lastIndexOf("/") + 1);
    }

}
