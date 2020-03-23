package pl.pk.flybooking.flybooking.segment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SegmentService {

    private SegmentRepository segmentRepository;

    public void addSegmentsFromList(Collection<Segment> segments, Set<Long> placesIds) {
        segmentRepository.saveAll(filterSegments(segments, placesIds));
    }

    public void clearSegmentTable() {
        if (segmentRepository.count() > 0)
            segmentRepository.deleteSegmentNative();
    }

    private List<Segment> filterSegments(Collection<Segment> segments, Set<Long> placesIds) {
        return segments.stream().filter(s -> placesIds.contains(s.getOriginStantion()) &&
                placesIds.contains(s.getDestinationStation()) && s.getDirectionality().equals("Outbound")).collect(Collectors.toList());
    }
}
