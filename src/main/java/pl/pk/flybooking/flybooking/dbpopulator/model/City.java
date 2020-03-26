package pl.pk.flybooking.flybooking.dbpopulator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "airports")
@EqualsAndHashCode
public class City {

    public interface JsonViews{
        interface get extends Country.JsonViews.get{}
    }

    @Id
    @JsonView(JsonViews.get.class)
    private String id;
    @JsonProperty("SingleAirportCity")
    boolean singleAirportCity;
    @OneToMany(mappedBy = "city")
    @JsonProperty("Airports")
    private List<Airport> airports;
    @ManyToOne
    private Country country;
    @JsonView(JsonViews.get.class)
    @JsonProperty("Location")
    private String location;
    @JsonView(JsonViews.get.class)
    @JsonProperty("IataCode")
    private String IATACode;
    @JsonView(JsonViews.get.class)
    @JsonProperty("Name")
    private String name;
}
