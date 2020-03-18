package pl.pk.flybooking.flybooking.segment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentParser implements Parser<Segment> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public List<Segment> parse(JsonNode jsonNodeData) throws JsonProcessingException {
        String segmentString  = objectMapper.writeValueAsString(jsonNodeData.get("Segments"));
        Segment[] asArray = objectMapper.readValue(segmentString, Segment[].class);
        return new ArrayList<>(Arrays.asList(asArray));
    }

//    @Override
//    public List<Segment> parse(JsonNode jsonNodeData) throws JsonProcessingException {
//        List<Segment> segments = new ArrayList<>();
//        jsonNodeData.get("Segments").forEach(n -> {
//
//            Segment segment = new Segment();
//
//            segment.setDirectionality(String.valueOf(n.get("Directionality")));
//            segment.setDepartureDateTime(String.valueOf(n.get("DepartureDateTime")));
//            segment.setArrivalDateTime(String.valueOf(n.get("ArrivalDateTime")));
//            segment.setArrivalDateTime(String.valueOf(n.get("JourneyMode")));
//            segment.setArrivalDateTime(String.valueOf(n.get("FlightNumber")));
//            segment.setArrivalDateTime(String.valueOf(n.get("ArrivalDateTime")));
//            segment.setOriginStantion(Long.valueOf(String.valueOf(n.get("OriginStation"))));
//            segment.setDestinationStation(Long.valueOf(String.valueOf(n.get("DestinationStation"))));
//            segment.setOperatingCarrier(Long.valueOf(String.valueOf(n.get("OperatingCarrier"))));
//            segment.setDuration(Integer.parseInt(String.valueOf(n.get("OriginStation"))));
//            segment.setCarrierId(Long.valueOf(String.valueOf(n.get("Carrier"))));
//            segment.setId(Long.valueOf(String.valueOf(n.get("Id"))));
//
//            segments.add(segment);
//        });
//        return segments;
//    }

}
