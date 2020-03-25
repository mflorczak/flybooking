package pl.pk.flybooking.flybooking.leg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leg {
    @Id
    @JsonProperty("Id")
    private String id;
    @ElementCollection
    @JsonProperty("FlightNumbers")
    private List<String> flights;
    @JsonProperty("Directionality")
    private String directionality;
}
