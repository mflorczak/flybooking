package pl.pk.flybooking.flybooking.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class SearchSessionService {

    private final static String UNIREST_POST_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0";
    private final static String X_RAPIDAPI_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private final static String X_RAPIDAPI_KEY = "caa574b2fdmsh4860ed5279957d0p1c2c5bjsn4f539dd8260d";
    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private com.fasterxml.jackson.databind.JsonNode jsonNodeData;

    public boolean getSessionKey() throws UnirestException {

        HttpResponse<JsonNode> response =
                Unirest.post(UNIREST_POST_URL)
                        .header("X-RapidAPI-Host", X_RAPIDAPI_HOST)
                        .header("X-RapidAPI-Key", X_RAPIDAPI_KEY)
                        .header("Content-Type", CONTENT_TYPE)

                        .field("country", "US")
                        .field("currency", "USD")
                        .field("locale", "en-US")
                        .field("originPlace", "SFO-sky")
                        .field("destinationPlace", "LHR-sky")
                        .field("outboundDate", "2020-03-14")
                        .field("adults", 1)
                        .field("inboundDate", "2020-03-31")
                        .asJson();

        if (null == response || response.getHeaders().toString().isEmpty()) {
            throw new UnirestException("Failed to create session");
        } else {
            String locationURL = response.getHeaders().getFirst("Location");
            getJsonDataFromSession(getLocationStringFromURL(locationURL));
            return true;
        }
    }

    private static String getLocationStringFromURL(String locationURL) {
        return locationURL.substring(locationURL.lastIndexOf("/") + 1);
    }

    private void getJsonDataFromSession(String sessionKey) throws UnirestException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpResponse<JsonNode> response = Unirest.get(UNIREST_POST_URL + sessionKey + "?pageIndex=0&pageSize=10")
                .header("X-RapidAPI-Host", X_RAPIDAPI_HOST)
                .header("X-RapidAPI-Key", X_RAPIDAPI_KEY).asJson();

        if (null == response || response.getHeaders().toString().isEmpty()) {
            throw new UnirestException("Failed to create session");
        } else {
            try {
                com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(response.getBody().toString());
                setJsonNodeData(jsonNode);

//                String json = objectMapper.writeValueAsString(jsonNode.get("Carriers"));

//                Carrier[] asArray = objectMapper.readValue(json, Carrier[].class);
//                List<Carrier> carriers = new ArrayList<>(Arrays.asList(asArray));
//                for (Carrier carrier : carriers) {
//                    System.out.println(carrier);
//                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setJsonNodeData(com.fasterxml.jackson.databind.JsonNode jsonNodeData) {
        this.jsonNodeData = jsonNodeData;
    }
}
