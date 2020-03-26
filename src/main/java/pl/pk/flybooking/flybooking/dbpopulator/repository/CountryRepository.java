package pl.pk.flybooking.flybooking.dbpopulator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pk.flybooking.flybooking.dbpopulator.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
