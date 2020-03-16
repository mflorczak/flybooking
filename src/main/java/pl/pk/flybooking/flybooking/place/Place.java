package pl.pk.flybooking.flybooking.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Place {
    @JsonProperty("ParentId")
    private Long parentId;
    @Id
    @JsonProperty("Id")
    private Long id;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Name")
    private String name;
}
