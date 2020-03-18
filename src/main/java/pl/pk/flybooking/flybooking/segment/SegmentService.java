package pl.pk.flybooking.flybooking.segment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SegmentService {

    private SegmentRepository segmentRepository;

    public void addSegmentsFromList(Collection<Segment> segments, Long originPlaceId, Long destinationPlaceId) {
        segmentRepository.saveAll(filterSegments(segments, originPlaceId, destinationPlaceId));
    }

    public void clearSegmentTable() {
        if (segmentRepository.count() > 0)
            segmentRepository.deleteSegmentNative();
    }

    private List<Segment> filterSegments(Collection<Segment> segments, Long originPlaceId, Long destinationPlaceId){
        return segments.stream().filter(s -> s.getOriginStantion().equals(originPlaceId) &&
                s.getDestinationStation().equals(destinationPlaceId)).collect(Collectors.toList());
    }
}
