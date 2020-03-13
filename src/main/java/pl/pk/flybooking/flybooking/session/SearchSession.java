package pl.pk.flybooking.flybooking.session;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

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
                .field("outboundDate", "2020-03-13")
                .field("adults", 1).field("inboundDate", "2020-03-31")
                .asJson();

        String locationUrl = response.getHeaders().getFirst("Location");
        return locationUrl.substring(locationUrl.lastIndexOf("/") + 1);
    }

    @GetMapping
    public void getResults() throws UnirestException, IOException {

        String sessionKey = createSession();

        HttpResponse<JsonNode> response = Unirest.get("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/"+ sessionKey +"?pageIndex=0&pageSize=10")
                .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d").asJson();

        String jsonString = prettyPrintJsonString(response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
        });

        map.forEach((k,v) -> System.out.println(k));

        for (Map.Entry<String,Object> entry: map.entrySet()){
            String k = entry.getKey();
            String v = entry.getValue().toString();
            if (k == "Carriers"){
                System.out.println(v);
            }
        }

        //System.out.println(map);

        BufferedWriter writer = new BufferedWriter(new FileWriter("a.json"));
        writer.write(prettyPrintJsonString(response.getBody()));
        writer.close();
    }

    @GetMapping("/search")
    public void getFlights(@RequestParam String country, @RequestParam String currency, @RequestParam String locale,
                           @RequestParam String originPlace, @RequestParam String destinationPlace, @RequestParam String outboundPartialDate,
                           @RequestParam(required = false) String inboundPartialDate) throws UnirestException, IOException {


        String queryString = country + "/" + currency + "/" + locale + "/" + originPlace + "/" + destinationPlace + "/" + outboundPartialDate  + "/" + inboundPartialDate;
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
