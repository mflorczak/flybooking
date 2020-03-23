package pl.pk.flybooking.flybooking.placesdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.placesdb.model.Airport;

import java.util.Set;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Airport findById(String id);
    Set<Airport> findAllByCity_Id(String cityId);
}
