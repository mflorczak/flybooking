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
import pl.pk.flybooking.flybooking.carrier.CarrierParser;
import pl.pk.flybooking.flybooking.carrier.CarrierService;
import pl.pk.flybooking.flybooking.flight.FlightService;
import pl.pk.flybooking.flybooking.parser.Parser;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.place.PlaceParser;
import pl.pk.flybooking.flybooking.place.PlaceService;
import pl.pk.flybooking.flybooking.placesdb.model.Airport;
import pl.pk.flybooking.flybooking.placesdb.repository.AirportRepository;
import pl.pk.flybooking.flybooking.placesdb.repository.CityRepository;
import pl.pk.flybooking.flybooking.segment.Segment;
import pl.pk.flybooking.flybooking.segment.SegmentParser;
import pl.pk.flybooking.flybooking.segment.SegmentService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    final ObjectMapper objectMapper = new ObjectMapper();

    private CarrierService carrierService;
    private PlaceService placeService;
    private SegmentService segmentService;
    private FlightService flightService;
    private CityRepository cityRepository;
    private AirportRepository airportRepository;

    //    private List<Parser> parsers;
//
//public Parser getParser(Class<T extends Parser> parserClass) {
//    parsers.stream().filter(p -> p instanceof Class<T>).findFirst().orElseThrow()
//}
//
//    private String formatCityName(String placeId) {
//        return cityRepository.findByName(placeId).getId() + "-sky";
//    }


    public String getSessionKey(String originPlaceId, @RequestParam String destinationPlaceId,
                                @RequestParam String outboundDate, @RequestParam String inboundDAte) throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.post(UNIREST_POST_URL)
                .header(UNIREST_POST_URL_HEADER, X_RAPIDAPI_HOST)
                .header(X_RAPIDAPI_KEY_HEADER, X_RAPIDAPI_KEY)
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE)
                .field(COUNTRY, "US")
                .field(CURRENCY, "USD")
                .field(LOCALE, "en-US")
                .field(ORIGIN_PLACE, originPlaceId + "-sky")
                .field(DESTINATION_PLACE, destinationPlaceId + "-sky")
                //.field(OUTBOUND_DATE, "2020-03-19")
                .field(OUTBOUND_DATE, outboundDate)
                .field(ADULTS, 1)
                //.field(INBOUND_DATE, "2020-03-31")
                .field(INBOUND_DATE, inboundDAte)
                .asJson();

        String locationURL = response.getHeaders().getFirst("Location");
        System.out.println(locationURL);
        return getLocationStringFromURL(locationURL);
    }

    public void clearDatabaseTables() {
        flightService.clearFlightTable();
        segmentService.clearSegmentTable();
        placeService.clearPlaceTable();
        carrierService.clearCarrierTable();
    }

    public Set<String> getAirportIds(String originPlaceId, String destinationPlaceId) {
        Set<Airport> originAirports = airportRepository.findAllByCity_Id(originPlaceId);
        Set<Airport> destinationAirports = airportRepository.findAllByCity_Id(destinationPlaceId);
        return Stream.concat(originAirports.stream(), destinationAirports.stream()).map(Airport::getId).collect(Collectors.toSet());
    }

    public Set<Long> getPlacesIds(String originPlaceId, String destinationPlaceId) {
        return placeService.getIdsByCode(getAirportIds(originPlaceId, destinationPlaceId));
    }

    public void dataFromFile() throws IOException, ParseException {

        clearDatabaseTables();
        String content = new String(Files.readAllBytes(Paths.get("a.json")));
        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(content);

        Parser<Carrier> carrierParser = new CarrierParser();
        carrierService.addCarriersFromList(carrierParser.parse(jsonNode));

        Set<String> placeCodes = getAirportIds("SFOA", "LOND");

        Parser<Place> placeParser = new PlaceParser();
        placeService.addPlacesFromList(placeParser.parse(jsonNode), placeCodes);

        Set<Long> placesIds = getPlacesIds("SFOA", "LOND");

        Parser<Segment> segmentParser = new SegmentParser();
        segmentService.addSegmentsFromList(segmentParser.parse(jsonNode), placesIds);

        flightService.createFlights();
    }

    //public void getJsonDataFromSession(String sessionKey) throws UnirestException, IOException {
    public void getJsonDataFromSession(String sessionKey, String originPlaceId, String destinationPlaceId) throws UnirestException, IOException, ParseException {

        clearDatabaseTables();

        HttpResponse<JsonNode> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/" + sessionKey + "?pageIndex=0&pageSize=10")
                .header("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("x-rapidapi-key", "220e772a27msha3e8d185aa8940bp1ccde7jsn5993ab18422e")
                .asJson();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());

        Parser<Carrier> carrierParser = new CarrierParser();
        carrierService.addCarriersFromList(carrierParser.parse(jsonNode));

        Set<String> placeCodes = getAirportIds("SFOA", "LOND");

        Parser<Place> placeParser = new PlaceParser();
        placeService.addPlacesFromList(placeParser.parse(jsonNode), placeCodes);

        Set<Long> placesIds = getPlacesIds("SFOA", "LOND");

        Parser<Segment> segmentParser = new SegmentParser();
        segmentService.addSegmentsFromList(segmentParser.parse(jsonNode), placesIds);

        flightService.createFlights();
    }


    private static String getLocationStringFromURL(String locationURL) {
        return locationURL.substring(locationURL.lastIndexOf("/") + 1);
    }

}
