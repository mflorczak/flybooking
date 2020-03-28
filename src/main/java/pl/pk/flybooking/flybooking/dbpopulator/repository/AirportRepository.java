package pl.pk.flybooking.flybooking.dbpopulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.dbpopulator.model.Airport;

import java.util.List;
import java.util.Set;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    List<Airport> findAllByIdIn(List<String> codes);

    @Query(value = "SELECT a FROM Airport a WHERE " +
            "a.city.id = :origin OR a.city.id = :destination")
    Set<Airport> findAirportsByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination);

    @Query(value = "SELECT a FROM Airport a WHERE a.id IN :places")
    List<Airport> findAllByPlacesIn(@Param("places")List<String> placeCodes);
}
