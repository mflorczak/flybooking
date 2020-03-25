package pl.pk.flybooking.flybooking.placesdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.placesdb.model.Airport;

import java.util.List;
import java.util.Set;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Airport findById(String id);

    Set<Airport> findAllByCity_Id(String cityId);

    List<Airport> findAllByIdIn(List<String> codes);
//
//    @Query(value = "SELECT Airport FROM Airport WHERE " +
//            "Airport.city = :origin OR Airport.city = :destination")
//    List<Airport> findAirportsByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);
}
