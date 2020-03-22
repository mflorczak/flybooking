package pl.pk.flybooking.flybooking.placesdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Continent {
    @Id
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String name;

    @OneToMany(mappedBy = "continent")
    @JsonProperty("Countries")
    private List<Country> countries;
}
