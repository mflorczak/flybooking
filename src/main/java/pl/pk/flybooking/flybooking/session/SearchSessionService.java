package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.carrier.CarrierService;
import pl.pk.flybooking.flybooking.carrier.CarrierParser;
import pl.pk.flybooking.flybooking.flight.FlightService;
import pl.pk.flybooking.flybooking.parser.Parser;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.place.PlaceParser;
import pl.pk.flybooking.flybooking.place.PlaceService;
import pl.pk.flybooking.flybooking.segment.Segment;
import pl.pk.flybooking.flybooking.segment.SegmentParser;
import pl.pk.flybooking.flybooking.segment.SegmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

@Service
@AllArgsConstructor
public class SearchSessionService {

    private final static String UNIREST_POST_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0";
    private final static String X_RAPIDAPI_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private final static String X_RAPIDAPI_KEY = "220e772a27msha3e8d185aa8940bp1ccde7jsn5993ab18422e";
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

    private CarrierService carrierService;
    private PlaceService placeService;
    private SegmentService segmentService;
    private FlightService flightService;

    public String getSessionKey() throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.post(UNIREST_POST_URL)
                .header(UNIREST_POST_URL_HEADER, X_RAPIDAPI_HOST)
                .header(X_RAPIDAPI_KEY_HEADER, X_RAPIDAPI_KEY)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                .field(COUNTRY, "US")
                .field(CURRENCY, "USD")
                .field(LOCALE, "en-US")
                .field(ORIGIN_PLACE, "SFO-sky")
                .field(DESTINATION_PLACE, "LHR-sky")
                .field(OUTBOUND_DATE, "2020-03-14")
                .field(ADULTS, 1)
                .field(INBOUND_DATE, "2020-03-31")
                .asJson();

        String locationURL = response.getHeaders().getFirst("Location");
        System.out.println(locationURL);
        return getLocationStringFromURL(locationURL);
    }

    public void dataFromFile() throws IOException, ParseException {
        String content = new String(Files.readAllBytes(Paths.get("a.json")));
        ObjectMapper objectMapper = new ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(content);

        Parser<Carrier> carrierParser = new CarrierParser();
        carrierService.addCarriersFromList(carrierParser.parse(jsonNode));
        Parser<Place> placeParser = new PlaceParser();
        placeService.addPlacesFromList(placeParser.parse(jsonNode));
        Parser<Segment> segmentParser = new SegmentParser();
        segmentService.addSegmentsFromList(segmentParser.parse(jsonNode));

        flightService.createFlights();
    }

    public void getJsonDataFromSession(String sessionKey) throws UnirestException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpResponse<JsonNode> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/734bf7ac-56b8-40cb-b787-b74f35e84fe1?pageIndex=0&pageSize=10")
                .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("x-rapidapi-key", "220e772a27msha3e8d185aa8940bp1ccde7jsn5993ab18422e")
                .asJson();

        //setJsonNodeData(response.getBody());
        //com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());
        //setJsonNode(jsonNode);
        //return objectMapper.writeValueAsString(jsonNode);
    }


    private static String getLocationStringFromURL(String locationURL) {
        return locationURL.substring(locationURL.lastIndexOf("/") + 1);
    }

}
