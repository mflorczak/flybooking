package pl.pk.flybooking.flybooking.flight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.place.Place;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface FlightRepository extends JpaRepository<Flight, Long> {
    public List<Flight> findAllByOriginStationAndDestinationStation(Place originStation, Place destinationStation);

    @Modifying
    @Query(value = "DELETE FROM Flight", nativeQuery = true)
    public void deleteFlightsNative();

   // @Query(value = "SELECT f from FLIGHT where f.origin_station_id in ids")
    public List<Flight> findAllByOriginStationIdInAndDestinationStationIdIn(Set<String> ids1, Set<String> ids2);


}
