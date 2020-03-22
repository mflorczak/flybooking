package pl.pk.flybooking.flybooking.placesdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.placesdb.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
