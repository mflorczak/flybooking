package pl.pk.flybooking.flybooking.carrier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.pk.flybooking.flybooking.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarrierParser implements Parser<Carrier> {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Carrier> parse(JsonNode jsonNodeData) throws JsonProcessingException {

        String carriersString = objectMapper.writeValueAsString(jsonNodeData.get("Carriers"));
        Carrier[] asArray = objectMapper.readValue(carriersString, Carrier[].class);
        List<Carrier> carriers = new ArrayList<>(Arrays.asList(asArray));
        return carriers;
    }
}
