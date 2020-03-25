package pl.pk.flybooking.flybooking.placesdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"cities", "continent"})
@Getter
@Setter
public class Country {

    public interface JsonViews {
        interface get {
        }
    }

    @Id
    @JsonProperty("Id")
    @JsonView(JsonViews.get.class)
    private String id;

    @JsonProperty("Name")
    @JsonView(JsonViews.get.class)
    private String name;

    @JsonProperty("CurrencyId")
    @JsonView(JsonViews.get.class)
    private String currencyId;

    @JsonProperty("Cities")
    @OneToMany(mappedBy = "country")
    private List<City> cities;

    @ManyToOne
    @JsonIgnore
    private Continent continent;
}
