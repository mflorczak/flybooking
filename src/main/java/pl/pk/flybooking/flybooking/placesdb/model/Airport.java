package pl.pk.flybooking.flybooking.placesdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Airport {

    public interface JsonViews{
        interface get extends City.JsonViews.get, Country.JsonViews.get{}
    }

    @Id
    @JsonProperty("Id")
    @JsonView(JsonViews.get.class)
    private String id;

    @ManyToOne
    @JsonView(JsonViews.get.class)
    private City city;

    @ManyToOne
    @JsonView(JsonViews.get.class)
    private Country country;

    @JsonProperty("Location")
    @JsonView(JsonViews.get.class)
    private String location;

    @JsonProperty("Name")
    @JsonView(JsonViews.get.class)
    private String name;
}
