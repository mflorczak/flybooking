package pl.pk.flybooking.flybooking.dbpopulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.dbpopulator.model.City;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE %:name%")
    List<City> findCitiesByName(String name);
}
