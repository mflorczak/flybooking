package pl.pk.flybooking.flybooking.placesdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Id
    @JsonProperty("Id")
    private String id;

    @ManyToOne
    @JsonIgnore
    private City city;

    @ManyToOne
    @JsonIgnore
    private Country country;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("Name")
    private String name;
}
