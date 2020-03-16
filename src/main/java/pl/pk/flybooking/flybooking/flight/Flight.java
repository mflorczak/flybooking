package pl.pk.flybooking.flybooking.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pk.flybooking.flybooking.carrier.Carrier;
import pl.pk.flybooking.flybooking.place.Place;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Place originStation;
    @OneToOne
    private Place desitnationStation;

    private Date departureDateTime;
    private Date arrivalDateTime;

    private String flightNumber;
    @OneToOne
    private Carrier carrier;
}
