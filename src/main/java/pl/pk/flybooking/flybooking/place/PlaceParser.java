package pl.pk.flybooking.flybooking.place;

import com.fasterxml.jackson.databind.JsonNode;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class PlaceParser implements Parser<Place> {
    @Override
    public List<Place> parse(JsonNode jsonNode) {
        List<Place> places = new ArrayList<>();
        jsonNode.get("Places").forEach(p -> {
            Place place = new Place();
            place.setId(p.get("Id").asLong());
            place.setType(p.get("Type").toPrettyString().replace("\"", ""));
            place.setName(p.get("Name").toPrettyString().replace("\"", ""));
            place.setCode(p.get("Code").toPrettyString().replace("\"", ""));
            places.add(place);
        });
        return places;
    }
}
