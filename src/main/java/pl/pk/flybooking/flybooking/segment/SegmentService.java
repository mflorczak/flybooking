package pl.pk.flybooking.flybooking.segment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SegmentService {

    private SegmentRepository segmentRepository;

    public void addSegmentsFromList(Collection<Segment> segments){
        for (Segment segment : segments){
            segmentRepository.save(segment);
        }
    }
}
