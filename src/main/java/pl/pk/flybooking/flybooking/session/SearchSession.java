package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.pk.flybooking.flybooking.Carrier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/session")
@AllArgsConstructor
public class SearchSession {

    public String prettyPrintJsonString(JsonNode jsonNode) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonNode.toString(), Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            return "Sorry, pretty print didn't work";
        }
    }

    @PostMapping()
    public String createSession() throws UnirestException {

        HttpResponse<JsonNode> response =
                Unirest.post("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0")
                        .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                        .header("X-RapidAPI-Key", "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .field("country", "US")
                        .field("currency", "USD")
                        .field("locale", "en-US")
                        .field("originPlace", "SFO-sky")
                        .field("destinationPlace", "LHR-sky")
                        .field("outboundDate", "2020-03-14")
                        .field("adults", 1).field("inboundDate", "2020-03-31")
                        .asJson();

        String locationUrl = response.getHeaders().getFirst("Location");
        System.out.println(locationUrl.substring(locationUrl.lastIndexOf("/") + 1));
        return locationUrl.substring(locationUrl.lastIndexOf("/") + 1);
    }

    @GetMapping
    public void getResults() throws UnirestException, IOException {

        String sessionKey = "d87c3a20-5683-4805-9ff3-abf16bf63110";

        HttpResponse<JsonNode> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/" + sessionKey + "?pageIndex=0&pageSize=10")
                .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d").asJson();

        ObjectMapper objectMapper = new ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());
        String json = objectMapper.writeValueAsString(jsonNode.get("Carriers"));

        Carrier[] asArray = objectMapper.readValue(json, Carrier[].class);
        List<Carrier> carriers = new ArrayList<>(Arrays.asList(asArray));
        for (Carrier carrier : carriers) {
            System.out.println(carrier);
        }

    }

    @GetMapping("/search")
    public void getFlights(@RequestParam String country, @RequestParam String currency, @RequestParam String locale,
                           @RequestParam String originPlace, @RequestParam String destinationPlace, @RequestParam String outboundPartialDate,
                           @RequestParam(required = false) String inboundPartialDate) throws UnirestException, IOException {


        String queryString = country + "/" + currency + "/" + locale + "/" + originPlace + "/" + destinationPlace + "/" + outboundPartialDate + "/" + inboundPartialDate;
        System.out.println(queryString);

        HttpResponse<JsonNode> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/" + queryString)
                .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d")
                .asJson();

        //System.out.println(response.getBody());
        BufferedWriter writer = new BufferedWriter(new FileWriter("aa.json"));
        writer.write(prettyPrintJsonString(response.getBody()));
        writer.close();
    }


}
