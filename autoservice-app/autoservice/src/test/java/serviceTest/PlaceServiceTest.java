package serviceTest;

import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.SortTypePlace;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.service.PlaceService;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
    @InjectMocks
    PlaceService placeService;
    @Mock
    MapperToEntity mapperToEntityMock;
    @Mock
    PlaceRepository placeRepositoryMock;
    @Mock
    MapperToDto mapperToDtoMock;

    @Test
    void addPlace_GoodAddPlace() {
        Place place = new Place("test");
        PlaceRequest placeRequest = new PlaceRequest("test");
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(placeRequest)).thenReturn(place);
        placeService.addPlace(placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).create(place);
    }

    @Test
    void addPlace_BadPlace() {
        Place place = new Place();
        PlaceRequest placeRequest = new PlaceRequest();
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(placeRequest)).thenReturn(place);
        placeService.addPlace(placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).create(place);
    }

    @Test
    void deletePlace() {
        int id = 1;
        Mockito.when(placeRepositoryMock.delete(id)).thenReturn(true);
        placeService.deletePlace(id);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).delete(id);
    }

    @Test
    void findPlace_RealPlace() {
        int id = 1;
        Place place = new Place("place");
        Mockito.when(placeRepositoryMock.find(id)).thenReturn(place);
        Mockito.when(mapperToDtoMock.mapToPlaceDto(place)).thenReturn(new PlaceDto());
        placeService.findPlace(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToPlaceDto(place);
    }

    @Test
    void findPlace_NullPlace() {
        int id = 1;
        Mockito.when(placeRepositoryMock.find(id)).thenReturn(null);
        Mockito.when(mapperToDtoMock.mapToPlaceDto(any())).thenReturn(new PlaceDto());
        placeService.findPlace(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToPlaceDto(null);
    }

    @Test
    void updatePlace() {
        int id = 1;
        PlaceRequest placeRequest = new PlaceRequest("test");
        Place place = new Place(id, placeRequest.getName());
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(id, placeRequest)).thenReturn(place);
        Mockito.doNothing().when(placeRepositoryMock).update(place);
        placeService.updatePlace(id, placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).update(place);
    }

    @Test
    void updatePlace_NoValidPlace() {
        int id = 1;
        PlaceRequest placeRequest = new PlaceRequest();
        Place place = new Place();
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(id, placeRequest)).thenReturn(place);
        Mockito.doNothing().when(placeRepositoryMock).update(place);
        placeService.updatePlace(id, placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).update(place);
    }

    @Test
    void getAllPlace() {
        Mockito.when(placeRepositoryMock.findAll(SortTypePlace.ID)).thenReturn(new ArrayList<>());
        placeService.getListOfPlace();
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).findAll(SortTypePlace.ID);
    }

}
