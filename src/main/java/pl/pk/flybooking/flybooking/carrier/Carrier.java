package pl.pk.flybooking.flybooking.carrier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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

    public interface JsonViews{
        interface get{};
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("Id")
    @JsonView(JsonViews.get.class)
    private Long id;
    @JsonProperty("ImageUrl")
    @JsonView(JsonViews.get.class)
    private String imageUrl;
    @JsonProperty("Code")
    @JsonView(JsonViews.get.class)
    private String code;
    @JsonProperty("Name")
    @JsonView(JsonViews.get.class)
    private String name;
    @JsonProperty("DisplayCode")
    @JsonView(JsonViews.get.class)
    private String displayCode;
}
