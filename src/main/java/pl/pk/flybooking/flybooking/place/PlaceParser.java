package pl.pk.flybooking.flybooking.place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceParser implements Parser<Place> {
    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Place> parse(JsonNode jsonNodeData) throws JsonProcessingException {
        String placeString = objectMapper.writeValueAsString(jsonNodeData.get("Places"));
        Place[] asArray = objectMapper.readValue(placeString, Place[].class);
        return new ArrayList<>(Arrays.asList(asArray));
    }
}
