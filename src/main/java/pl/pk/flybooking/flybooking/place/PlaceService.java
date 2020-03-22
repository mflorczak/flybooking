package pl.pk.flybooking.flybooking.place;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaceService {
    private PlaceRepository placeRepository;

    public void addPlacesFromList(Collection<Place> places, Long originPlaceId, Long destinationPlaceId) {
        placeRepository.saveAll(filterPlaces(places, originPlaceId, destinationPlaceId));
    }

    public void clearPlaceTable() {
        if (placeRepository.count() > 0)
            placeRepository.deleteAll();
    }

    private List<Place> filterPlaces(Collection<Place> places, Long originPlaceId, Long destinationPlaceId) {
        return places.stream().filter(p -> p.getId().equals(originPlaceId) || p.getId().equals(destinationPlaceId)
        ).collect(Collectors.toList());
    }
}
