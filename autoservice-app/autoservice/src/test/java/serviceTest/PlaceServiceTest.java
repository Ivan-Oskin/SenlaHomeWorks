package serviceTest;

import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.repository.PlaceRepository;
import com.oskin.autoservice.service.PlaceService;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
    @InjectMocks
    PlaceService placeService;
    @Mock
    MapperToEntity mapperToEntityMock;
    @Mock
    PlaceRepository placeRepositoryMock;

    Place place = new Place("test");
    PlaceRequest placeRequest = new PlaceRequest("test");

    @Test
    void addPlace_GoodAddPlace() {
        Mockito.when(mapperToEntityMock.mapToPlaceEntity(placeRequest)).thenReturn(place);
        placeService.addPlace(placeRequest);
        Mockito.verify(placeRepositoryMock, Mockito.times(1)).create(place);
    }
}
