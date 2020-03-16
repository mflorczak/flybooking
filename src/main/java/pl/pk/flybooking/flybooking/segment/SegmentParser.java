package pl.pk.flybooking.flybooking.segment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentParser implements Parser<Segment> {
    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Segment> parse(JsonNode jsonNodeData) throws JsonProcessingException {
        String segmentString  = objectMapper.writeValueAsString(jsonNodeData.get("Segments"));
        Segment[] asArray = objectMapper.readValue(segmentString, Segment[].class);
        return new ArrayList<>(Arrays.asList(asArray));
    }
}
