package pl.pk.flybooking.flybooking.carrier;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Carrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("Id")
    private Long id;
    @JsonProperty("ImageUrl")
    private String imageUrl;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("DisplayCode")
    private String displayCode;
}
