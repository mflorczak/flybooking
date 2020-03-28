package pl.pk.flybooking.flybooking.segment;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Component
public class SegmentParser implements Parser<Segment> {

    @Override
    public List<Segment> parse(JsonNode jsonNode) {
        List<Segment> segments = new ArrayList<>();
        jsonNode.get("Segments").forEach(n -> {

            Segment segment = new Segment();

            segment.setDirectionality((n.get("Directionality").toPrettyString().replace("\"", "")));
            segment.setDepartureDateTime(n.get("DepartureDateTime").toPrettyString().replace("\"", ""));
            segment.setArrivalDateTime(n.get("ArrivalDateTime").toPrettyString().replace("\"", ""));
            segment.setJourneyMode(n.get("JourneyMode").toPrettyString().replace("\"", ""));
            segment.setFlightNumber(n.get("FlightNumber").toPrettyString().replace("\"", ""));
            segment.setOriginStantion(n.get("OriginStation").asLong());
            segment.setDestinationStation((n.get("DestinationStation").asLong()));
            segment.setDuration(Integer.parseInt(String.valueOf(n.get("OriginStation"))));
            segment.setCarrierId((n.get("Carrier").asLong()));
            segment.setId((n.get("Id").asLong()));

            segments.add(segment);
        });
        return segments;
    }
}
