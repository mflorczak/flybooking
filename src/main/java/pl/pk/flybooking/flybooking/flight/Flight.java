package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.placesdb.model.Airport;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flight {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Airport originStation;
    @OneToOne
    private Airport destinationStation;

    private Date departureDateTime;
    private Date arrivalDateTime;

    private String flightNumber;
    @OneToOne
    private Carrier carrier;
}
