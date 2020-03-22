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
public class Country {
    @Id
    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("CurrencyId")
    private String currencyId;

    @JsonProperty("Cities")
    @OneToMany(mappedBy = "country")
    private List<City> cities;

    @ManyToOne
    private Continent continent;
}
