package pl.pk.flybooking.flybooking.carrier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Set<Carrier> findAllByIdIn(Set<Long> carrierIds);
}
