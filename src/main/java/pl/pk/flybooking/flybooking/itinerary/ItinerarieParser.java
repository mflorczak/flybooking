package pl.pk.flybooking.flybooking.itinerary;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItinerarieParser implements Parser<Itinerary> {
    @Override
    public List<Itinerary> parse(JsonNode jsonNode) {
        List<Itinerary> itineraries = new ArrayList<>();
        jsonNode.get("Itineraries").forEach(n -> {
            Itinerary itinerary = new Itinerary();
            itinerary.setInboundLegId(n.get("InboundLegId").toPrettyString().replace("\"", ""));
            itinerary.setOutboundLegId(n.get("OutboundLegId").toPrettyString().replace("\"", ""));
            n.get("PricingOptions").forEach(p -> itinerary.setPrice(p.get("Price").toPrettyString().replace("\"", "")));
            itineraries.add(itinerary);
        });
        return itineraries;
    }
}
