package pl.pk.flybooking.flybooking.carrier;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CarrierService {

    private CarrierRepository carrierRepository;

    public void addCarriersFromList(Collection<Carrier> carriers){
        if (carrierRepository.count() > 0)
        carrierRepository.saveAll(carriers);
    }

    public void clearCarrierTable(){
        carrierRepository.deleteAll();
    }
}
