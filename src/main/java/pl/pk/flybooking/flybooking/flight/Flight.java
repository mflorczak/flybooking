package pl.pk.flybooking.flybooking.flight;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.dbpopulator.model.Airport;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flight {

    public interface JsonViews{
        interface get extends Airport.JsonViews.get{}
    }

    @Id
    @JsonView(JsonViews.get.class)
    private Long id;
    @OneToOne
    @JsonView(JsonViews.get.class)
    private Carrier carrier;
    @OneToOne
    @JsonView(JsonViews.get.class)
    private Airport originStation;
    @OneToOne
    @JsonView(JsonViews.get.class)
    private Airport destinationStation;
    @JsonView(JsonViews.get.class)
    private Date departureDateTime;
    @JsonView(JsonViews.get.class)
    private Date arrivalDateTime;
    @JsonView(JsonViews.get.class)
    private String flightNumber;
    @JsonView(JsonViews.get.class)
    private String directionality;
}
