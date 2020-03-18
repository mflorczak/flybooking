package pl.pk.flybooking.flybooking.place;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PlaceService {
    private PlaceRepository placeRepository;

    public void addPlacesFromList(Collection<Place> places) {
        placeRepository.saveAll(places);
    }

    public void clearPlaceTable() {
        if (placeRepository.count() > 0)
            placeRepository.deleteAll();
    }

    //private List<Place> filterPlacesById(){

    //}
}
