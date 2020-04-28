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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final CarrierRepository carrierRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public User bookFlights(String usernameOrEmail, List<Flight> flights) {
        Set<Flight> flightSet = new HashSet<>();
        User userDb = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new GenericValidationException("userNotFound", usernameOrEmail));

        saveCarriersFromFlights(flights);
        addNewFlightsToSet(flightSet, flights, userDb);
        addNewFlightsToDb(flightSet);

        userDb.getFlights().addAll(flightSet);
        return userDb;
    }

    private void addNewFlightsToDb(Set<Flight> flightSet) {
        flightSet.forEach(flight -> {
            if (!flightRepository.existsByFlightNumberAndArrivalDateTimeAndDepartureDateTime(flight.getFlightNumber(),
                    flight.getArrivalDateTime(), flight.getDepartureDateTime())) {
                flightRepository.save(flight);
            }
        });
    }

    private void addNewFlightsToSet(Set<Flight> flightSet, List<Flight> flights, User user) {
        flights.forEach(flight -> {
            if (!user.getFlights().contains(flight)) {
                flightSet.add(flight);
            } else {
                throw new IllegalArgumentException("flight " + flight.getFlightNumber() + " already reserved");
            }
        });
    }

    private void saveCarriersFromFlights(List<Flight> flights) {
        carrierRepository.saveAll(flights.stream().map(Flight::getCarrier).collect(Collectors.toSet()));

    }
}
