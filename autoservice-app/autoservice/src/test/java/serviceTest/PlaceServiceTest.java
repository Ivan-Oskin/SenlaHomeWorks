package serviceTest;

import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.SortTypePlace;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.service.PlaceService;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.BeforeEach;
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

    Place place;
    PlaceRequest placeRequest;
    int id;

    @BeforeEach
    void setUp() {
        placeRequest = new PlaceRequest("test");
        place = new Place(placeRequest.getName());
        id = 1;
    }

    @Test
    void addPlace_GoodAddPlace() {
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(placeRequest)).thenReturn(place);
        placeService.addPlace(placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).create(place);
    }

    @Test
    void addPlace_BadPlace() {
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(placeRequest)).thenReturn(place);
        placeService.addPlace(placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).create(place);
    }

    @Test
    void deletePlace() {
        Mockito.when(placeRepositoryMock.delete(id)).thenReturn(true);
        placeService.deletePlace(id);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).delete(id);
    }

    @Test
    void findPlace_RealPlace() {
        Mockito.when(placeRepositoryMock.find(id)).thenReturn(place);
        Mockito.when(mapperToDtoMock.mapToPlaceDto(place)).thenReturn(new PlaceDto());
        placeService.findPlace(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToPlaceDto(place);
    }

    @Test
    void findPlace_NullPlace() {
        Mockito.when(placeRepositoryMock.find(id)).thenReturn(null);
        Mockito.when(mapperToDtoMock.mapToPlaceDto(any())).thenReturn(new PlaceDto());
        placeService.findPlace(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToPlaceDto(null);
    }

    @Test
    void updatePlace() {
        Place placeUpdate = new Place(id, placeRequest.getName());
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(id, placeRequest)).thenReturn(placeUpdate);
        placeService.updatePlace(id, placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).update(placeUpdate);
    }

    @Test
    void updatePlace_NoValidPlace() {
        Place placeUpdate = new Place(id, placeRequest.getName());
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(id, placeRequest)).thenReturn(placeUpdate);
        placeService.updatePlace(id, placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).update(placeUpdate);
    }

    @Test
    void getAllPlace() {
        Mockito.when(placeRepositoryMock.findAll(SortTypePlace.ID)).thenReturn(new ArrayList<>());
        placeService.getListOfPlace();
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).findAll(SortTypePlace.ID);
    }

}
