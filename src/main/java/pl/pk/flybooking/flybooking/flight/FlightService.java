package pl.pk.flybooking.flybooking.flight;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.dbpopulator.model.Airport;
import pl.pk.flybooking.flybooking.dbpopulator.repository.AirportRepository;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.segment.Segment;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class FlightService {
    private final static String PLACE_TYPE = "Airport";

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;

    public List<Flight> createFlights(List<Carrier> carriers, List<Place> places, List<Segment> segments) {
        Map<Long, Carrier> carriersByIds = Maps.uniqueIndex(carriers, Carrier::getId);
        Map<Long, Airport> airportsByIds = getAirportsByPlacesIds(places);

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

    private Map<Long, Airport> getAirportsByPlacesIds(List<Place> places) {
        Map<String, Long> filteredAirportCodesByPlaceIds = places.stream()
                .filter(p -> p.getType().equals(PLACE_TYPE))
                .collect(Collectors.toMap(Place::getCode, Place::getId));

        Map<String, Airport> airportsByCodes = Maps.uniqueIndex(airportRepository.findAllByPlacesIn(places.stream()
                .map(Place::getCode)
                .collect(Collectors.toList())), Airport::getId);

        return filteredAirportCodesByPlaceIds.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, entry -> airportsByCodes.get(entry.getKey())));
    }
}
