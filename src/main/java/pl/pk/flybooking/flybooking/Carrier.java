package pl.pk.flybooking.flybooking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Carrier {
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
