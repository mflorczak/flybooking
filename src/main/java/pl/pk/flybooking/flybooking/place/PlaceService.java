package pl.pk.flybooking.flybooking.place;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaceService {
    private PlaceRepository placeRepository;

    public void addPlacesFromList(Collection<Place> places, Set<String> codes) {
        placeRepository.saveAll(filterPlaces(places, codes));
        //placeRepository.saveAll(places);
    }

    public void clearPlaceTable() {
        if (placeRepository.count() > 0)
            placeRepository.deleteAll();
    }

    private List<Place> filterPlaces(Collection<Place> places, Set<String> placesIds) {
//        return places.stream().filter(p -> p.getId().equals(originPlaceId) || p.getId().equals(destinationPlaceId)
//        ).collect(Collectors.toList());
        return places.stream().filter(p -> placesIds.contains(p.getCode())).collect(Collectors.toList());
    }

    public Set<Long> getIdsByCode(Set<String> codes) {
        return placeRepository.findAllByCodeIn(codes).stream().map(Place::getId).collect(Collectors.toSet());
    }

    public List<Place> getAll(){
        return placeRepository.findAll();
    }

}
