package pl.pk.flybooking.flybooking.segment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    @Modifying
    @Query(value = "DELETE FROM Segment", nativeQuery = true)
    void deleteSegmentNative();
}
