package pl.pk.flybooking.flybooking.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Set<Place> findAllByIdIn(Set<Long> ids);
    Set<Place> findAllByCodeIn(Set<String> codes);
    @Modifying
    @Query(value = "DELETE FROM Place", nativeQuery = true)
    void deletePlacesNative();



}
