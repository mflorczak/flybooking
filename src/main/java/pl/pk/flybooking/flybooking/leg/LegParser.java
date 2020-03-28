package pl.pk.flybooking.flybooking.leg;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LegParser implements Parser<Leg> {
    @Override
    public List<Leg> parse(JsonNode jsonNode) {

        List<Leg> legs = new ArrayList<>();

        jsonNode.get("Legs").forEach(l -> {
            Leg leg = new Leg();
            leg.setId(l.get("Id").toPrettyString().replace("\"", ""));
            leg.setDirectionality(l.get("Directionality").toPrettyString().replace("\"", ""));
            List<String> numbers = new ArrayList<>();
            l.get("FlightNumbers").forEach(n -> {
                numbers.add(n.get("FlightNumber").toPrettyString().replace("\"", ""));
            });
            leg.setFlights(numbers);
            legs.add(leg);
        });

        return legs.stream()
                .filter(l -> l.getFlights().size() == 1)
                .collect(Collectors.toList());
    }
}
