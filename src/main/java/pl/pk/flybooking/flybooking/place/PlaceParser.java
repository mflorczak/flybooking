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
//        List<Place> places = new ArrayList<>();
//        jsonNodeData.get("Places").forEach(n -> {
//            Place place = new Place();
//
//            place.setCode(String.valueOf(n.get("Code")));
//            place.setName(String.valueOf(n.get("Name")));
//            place.setId(Long.valueOf(String.valueOf(n.get("Id"))));
//            //place.setParentId(Long.valueOf(String.valueOf(n.get("ParentId"))));
//            place.setType(String.valueOf(n.get("Type")));
//
//            places.add(place);
//        });
//
//        return places;
        String placeString = objectMapper.writeValueAsString(jsonNodeData.get("Places"));
        Place[] asArray = objectMapper.readValue(placeString, Place[].class);
        return new ArrayList<>(Arrays.asList(asArray));
    }
}
