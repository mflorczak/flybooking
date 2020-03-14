package pl.pk.flybooking.flybooking.carrier;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CarrierService {

    private CarrierRepository carrierRepository;

    public void addCarriersList(Collection<Carrier> carriers){
        for(Carrier carrier : carriers){
            carrierRepository.save(carrier);
        }
    }
}
