package pl.pk.flybooking.flybooking.placesdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    private String id;
    @JsonProperty("SingleAirportCity")
    boolean singleAirportCity;
    @OneToMany(mappedBy = "city")
    @JsonProperty("Airports")
    private List<Airport> airports;
    @ManyToOne
    private Country country;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("IataCode")
    private String IATACode;
    @JsonProperty("Name")
    private String Name;
}
