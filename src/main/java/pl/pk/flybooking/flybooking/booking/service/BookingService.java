package pl.pk.flybooking.flybooking.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightService;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final FlightService flightService;

    @Transactional
    public User bookFlights(String  usernameOrEmail, List<Flight> flights) {
        User userDb = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new GenericValidationException("userNotFound", usernameOrEmail));

        List<Flight> flightList = flights.stream()
                .map(flight -> flightService.findFlightByFlightNumber(flight.getFlightNumber())
                        .orElse(flightService.saveFlight(flight))
        ).collect(Collectors.toList());

        userDb.setFlights(new HashSet<>(flightList));

        return userDb;
    }
}
