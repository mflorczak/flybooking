package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.CarrierRepository;
import pl.pk.flybooking.flybooking.place.PlaceRepository;
import pl.pk.flybooking.flybooking.segment.Segment;
import pl.pk.flybooking.flybooking.segment.SegmentRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightService {
    private FlightRepository flightRepository;
    private CarrierRepository carrierRepository;
    private PlaceRepository placeRepository;
    private SegmentRepository segmentRepository;

    public List<Flight> createFlights() throws ParseException {
        List<Flight> flights = new ArrayList<>();
        List<Segment> segments = segmentRepository.findAll();


        for (Segment segment : segments){
            Flight flight = new Flight();

            flight.setOriginStation(placeRepository.findById(segment.getOriginStantion()).get());
            flight.setDesitnationStation(placeRepository.findById(segment.getDestinationStation()).get());
            flight.setDepartureDateTime(formatDate(segment.getDepartureDateTime()));
            flight.setArrivalDateTime(formatDate(segment.getArrivalDateTime()));

            flights.add(flight);
        }
        flights.forEach(System.out::println);
        return flights;
    }

    private Date formatDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.parse(date);
    }
}
