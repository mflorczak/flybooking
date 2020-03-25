package pl.pk.flybooking.flybooking.flight;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.placesdb.model.Airport;
import pl.pk.flybooking.flybooking.placesdb.repository.AirportRepository;
import pl.pk.flybooking.flybooking.segment.Segment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlightService {

    private FlightRepository flightRepository;

    private AirportRepository airportRepository;

    //    private Map<Long, Place> getPlacesById(List<Segment> segments) {
//        Set<Long> placesIds = new HashSet<>();
//        segments.forEach(s -> {
//            placesIds.add(s.getOriginStantion());
//            placesIds.add(s.getDestinationStation());
//        });
//        Set<Place> places = placeRepository.findAllByIdIn(placesIds);
//        Map<Long, Place> placesByIds = new HashMap<>();
//        places.forEach(p -> placesByIds.put(p.getId(), p));
//        return placesByIds;
//    }
//
//    private Map<Long, Carrier> getCarriersById(List<Segment> segments){
//        Set<Long> carrierIds = new HashSet<>();
//        segments.forEach(s -> carrierIds.add(s.getCarrierId()));
//        Set<Carrier> carriers = carrierRepository.findAllByIdIn(carrierIds);
//        Map<Long, Carrier> carriersById = new HashMap<>();
//        carriers.forEach(c -> carriersById.put(c.getId(), c));
//        return carriersById;
//    }

    private Map<Long, Airport> airportsByPlacesIds(Map<Long, Place> places) {
        Map<Long, String> codesByIds = places.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getCode()));
        List<Airport> usedAirports = airportRepository.findAllByIdIn(new ArrayList<>(codesByIds.values()));
        Map<String, Airport> airportsByCodes = Maps.uniqueIndex(usedAirports, Airport::getId);

        Map<Long, String> usedCodes = codesByIds.entrySet().stream().filter(entry -> airportsByCodes.containsKey(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return usedCodes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> airportsByCodes.get(entry.getValue())));
    }

    @SneakyThrows
    public List<Flight> createFlights(List<Carrier> carriers, List<Place> places, List<Segment> segments) {
        Map<Long, Carrier> carriersByIds = Maps.uniqueIndex(carriers, Carrier::getId);
        Map<Long, Airport> airportsByIds = airportsByPlacesIds(Maps.uniqueIndex(places, Place::getId));
        List<Flight> flights = new ArrayList<>();

        segments.forEach(s -> {
            Flight flight = new Flight();
            flight.setId(s.getId());
            flight.setCarrier(carriersByIds.get(s.getCarrierId()));
            flight.setOriginStation(airportsByIds.get(s.getOriginStantion()));
            flight.setDestinationStation(airportsByIds.get(s.getDestinationStation()));
            flight.setArrivalDateTime(formatDate(s.getArrivalDateTime()));
            flight.setDepartureDateTime(formatDate(s.getDepartureDateTime()));
            flight.setFlightNumber(s.getFlightNumber());
            flight.setDirectionality(s.getDirectionality());
            flights.add(flight);
        });
        return flights;
    }

    public void saveFlights(List<Flight> flights) {
        flightRepository.saveAll(flights);
    }

    @SneakyThrows
    private Date formatDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.parse(date);
    }
}
