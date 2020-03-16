package pl.pk.flybooking.flybooking.segment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Segment {
    @JsonProperty("Directionality")
    private String directionality;
    @JsonProperty("OriginStation")
    private Long originStantion;
    @JsonProperty("DepartureDateTime")
    private String departureDateTime;
    @JsonProperty("ArrivalDateTime")
    private String arrivalDateTime;
    @JsonProperty("JourneyMode")
    private String journeyMode;
    @JsonProperty("DestinationStation")
    private Long destinationStation;
    @JsonProperty("OperatingCarrier")
    private Long OperatingCarrier;
    @JsonProperty("FlightNumber")
    private String flightNumber;
    @JsonProperty("Duration")
    private int duration;
    @Id
    @JsonProperty("Id")
    private Long id;
    @JsonProperty("Carrier")
    private Long carrierId;
}
