package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.CarrierRepository;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.place.PlaceRepository;
import pl.pk.flybooking.flybooking.segment.Segment;
import pl.pk.flybooking.flybooking.segment.SegmentRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class FlightService {
    private FlightRepository flightRepository;
    private CarrierRepository carrierRepository;
    private PlaceRepository placeRepository;
    private SegmentRepository segmentRepository;

    private Map<Long, Place> getPlacesById(List<Segment> segments) {
        Set<Long> placesIds = new HashSet<>();
        segments.forEach(s -> {
            placesIds.add(s.getOriginStantion());
            placesIds.add(s.getDestinationStation());
        });

        Set<Place> places = placeRepository.findAllByIdIn(placesIds);
        Map<Long, Place> placesByIds = new HashMap<>();
        places.forEach(p -> placesByIds.put(p.getId(), p));
        return placesByIds;
    }


    public List<Flight> createFlights() throws ParseException {
        List<Flight> flights = new ArrayList<>();
        List<Segment> segments = segmentRepository.findAll();
        Map<Long, Place> placesByIds = getPlacesById(segments);

        for (Segment segment : segments) {
            Flight flight = new Flight();
            flight.setId(segment.getId());
            flight.setOriginStation(placesByIds.get(segment.getOriginStantion()));
            flight.setDestinationStation(placesByIds.get(segment.getDestinationStation()));
            flight.setDepartureDateTime(formatDate(segment.getDepartureDateTime()));
            flight.setArrivalDateTime(formatDate(segment.getArrivalDateTime()));
            flight.setFlightNumber(segment.getFlightNumber());
            // todo query as above to one map
            //flight.setCarrier(carrierRepository.findById(segment.getCarrierId()).get());
            flights.add(flight);


        }
        flightRepository.saveAll(flights);
        return flights;
    }

    public List<Flight> getFlightsByPlaces(Long destinationStationId, Long originStationId) {
        return flightRepository.findAllByOriginStationAndDestinationStation(placeRepository.findById(originStationId).get(), placeRepository.findById(destinationStationId).get());
    }

    private Date formatDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.parse(date);
    }

    public void clearFlightTable() {
        if (flightRepository.count() > 0)
            flightRepository.deleteFlightsNative();
    }

    public List <Flight> getAllFlights(){
        return flightRepository.findAll();
    }
}
