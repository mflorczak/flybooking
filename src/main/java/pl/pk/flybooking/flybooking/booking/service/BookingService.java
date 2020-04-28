package pl.pk.flybooking.flybooking.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.carrier.CarrierRepository;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightRepository;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final CarrierRepository carrierRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public User bookFlights(String usernameOrEmail, List<Flight> flights) {
        User userDb = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new GenericValidationException("userNotFound", usernameOrEmail));

        carrierRepository.saveAll(flights.stream().map(Flight::getCarrier).collect(Collectors.toSet()));

        Set<Flight> flightSet = new HashSet<>();
        flights.forEach(flight -> {
            if(!userDb.getFlights().contains(flight)){
                flightSet.add(flight);
            }
        });

        flightSet.forEach(flight -> {
            if (!flightRepository.existsByFlightNumberAndArrivalDateTimeAndDepartureDateTime(flight.getFlightNumber(),
                    flight.getArrivalDateTime(), flight.getDepartureDateTime())) {
                flightRepository.save(flight);
            }
        });

        System.out.println(flightSet);
        userDb.getFlights().addAll(flightSet);
        return userDb;
    }
}
