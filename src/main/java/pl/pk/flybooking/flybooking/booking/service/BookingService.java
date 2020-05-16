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

@Service
@AllArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final CarrierRepository carrierRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public User bookFlights(String usernameOrEmail, Flight flight) {
        Set<Flight> flightSet = new HashSet<>();
        User userDb = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new GenericValidationException("userNotFound", usernameOrEmail));

        saveCarriersFromFlights(flight);
        addNewFlightsToSet(flightSet, flight, userDb);
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

    private void addNewFlightsToSet(Set<Flight> flightSet, Flight flight, User user) {
        if (!user.getFlights().contains(flight)) {
            flightSet.add(flight);
        } else {
            throw new GenericValidationException("flightIsAlreadyReserved", flight.getFlightNumber());
        }
    }

    private void saveCarriersFromFlights(Flight flight) {
        carrierRepository.save(flight.getCarrier());
    }

    public User bookedUserFlights(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new GenericValidationException("userNotFound", usernameOrEmail));
    }

    @Transactional
    public void cancelFlight(String userEmail, Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new GenericValidationException("flightNotFound", flightId.toString()));
        User user = bookedUserFlights(userEmail);
        user.removeFlight(flight);
    }
}
