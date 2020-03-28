package pl.pk.flybooking.flybooking.itinerary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Itinerary {
    @JsonProperty("InboundLegId")
    private String inboundLegId;
    @JsonProperty("OutboundLegId")
    private String outboundLegId;
    @JsonProperty("Price")
    private String price;
    private String inboundFlightNumber;
    private String outboundFlightNumber;
}
