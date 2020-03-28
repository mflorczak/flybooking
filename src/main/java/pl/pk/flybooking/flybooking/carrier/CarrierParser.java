package pl.pk.flybooking.flybooking.carrier;

import org.springframework.stereotype.Component;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarrierParser implements Parser<Carrier> {
    @Override
    public List<Carrier> parse(com.fasterxml.jackson.databind.JsonNode jsonNode) {
        List<Carrier> carriers = new ArrayList<>();
        jsonNode.get("Carriers").forEach(c -> {
            Carrier carrier = new Carrier();
            carrier.setId(c.get("Id").asLong());
            carrier.setCode(c.get("Code").toPrettyString().replace("\"", ""));
            carrier.setDisplayCode(c.get("DisplayCode").toPrettyString().replace("\"", ""));
            carrier.setImageUrl(c.get("ImageUrl").toPrettyString().replace("\"", ""));
            carrier.setName(c.get("Name").toPrettyString().replace("\"", ""));
            carriers.add(carrier);
        });
        return carriers;
    }
}
