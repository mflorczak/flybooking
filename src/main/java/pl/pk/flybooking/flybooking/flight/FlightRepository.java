package pl.pk.flybooking.flybooking.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
@Transactional
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);
    Boolean existsByFlightNumber(String flightNumber);
    Boolean existsByFlightNumberAndArrivalDateTimeAndDepartureDateTime(String flightNumber, Date arrivalDateTime, Date departureDateTime);
}
