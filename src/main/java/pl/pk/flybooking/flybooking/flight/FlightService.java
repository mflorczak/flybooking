package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.carrier.CarrierRepository;
import pl.pk.flybooking.flybooking.place.Place;
import pl.pk.flybooking.flybooking.place.PlaceRepository;
import pl.pk.flybooking.flybooking.placesdb.repository.AirportRepository;
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
    private AirportRepository airportRepository;

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

    private Map<Long, Carrier> getCarriersById(List<Segment> segments){
        Set<Long> carrierIds = new HashSet<>();
        segments.forEach(s -> carrierIds.add(s.getCarrierId()));
        Set<Carrier> carriers = carrierRepository.findAllByIdIn(carrierIds);
        Map<Long, Carrier> carriersById = new HashMap<>();
        carriers.forEach(c -> carriersById.put(c.getId(), c));
        return carriersById;
    }


    public void createFlights() throws ParseException {
        //List<Flight> flights = new ArrayList<>();
        List<Segment> segments = segmentRepository.findAll();
        Map<Long, Place> placesByIds = getPlacesById(segments);
        Map<Long, Carrier> carriersById = getCarriersById(segments);

        for (Segment segment : segments) {
            Flight flight = new Flight();
            flight.setId(segment.getId());
            flight.setOriginStation(airportRepository.findById(placesByIds.get(segment.getOriginStantion()).getCode()));
            flight.setDestinationStation(airportRepository.findById(placesByIds.get(segment.getDestinationStation()).getCode()));
            flight.setDepartureDateTime(formatDate(segment.getDepartureDateTime()));
            flight.setArrivalDateTime(formatDate(segment.getArrivalDateTime()));
            flight.setFlightNumber(segment.getFlightNumber());
            flight.setCarrier(carriersById.get(segment.getCarrierId()));
            //flights.add(flight);
            flightRepository.save(flight);
        }
        //flightRepository.saveAll(flights);
        //return flights;
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

    public List<Flight> getFlightsByPlaceIds(Set<String> ids){
        return flightRepository.findAllByOriginStationIdInAndDestinationStationIdIn(ids, ids);
    }
}
