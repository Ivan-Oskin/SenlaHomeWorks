package serviceTest;

import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.MasterService;
import com.oskin.autoservice.service.OrderService;
import com.oskin.autoservice.service.PlaceService;
import com.oskin.autoservice.utils.MapperToDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DateServiceTest {
    @Spy
    @InjectMocks
    DateService dateService;
    @Mock
    MasterService masterServiceMock;
    @Mock
    OrderService orderServiceMock;
    @Mock
    PlaceService placeServiceMock;
    @Mock
    MapperToDto mapperToDtoMock;

    @Test
    void getNearestDate_SetEmptyListMaster_ReturnNull() {
        Mockito.when(masterServiceMock.getListOfMasters(SortTypeMaster.ID)).thenReturn(new ArrayList<>());
        DateDto date = dateService.getNearestDate();
        Assertions.assertNull(date);
    }

    @Test
    void getNearestDate_GoodMasterAndOrder_ReturnDateNow() {
        ArrayList<Master> masters = new ArrayList<>(Collections.singleton(new Master()));
        ArrayList<Order> orders = new ArrayList<>(Collections.singleton(new Order()));
        Mockito.when(masterServiceMock.getListOfMasters(SortTypeMaster.ID)).thenReturn(masters);
        Mockito.when(orderServiceMock.getListOfOrders(SortTypeOrder.ID)).thenReturn(orders);
        Mockito.doReturn(1).when(dateService).getCountFreePlaceInDate(any(LocalDateTime.class));
        dateService.getNearestDate();
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToDateDto(any());
    }

    @Test
    void getCountFreePlaceInDate_NoPlaceInSystem_NullPlaces() {
        Mockito.doReturn(new ArrayList<>()).when(dateService).getFreePlaceInDate(any(LocalDateTime.class));
        int count = dateService.getCountFreePlaceInDate(LocalDateTime.now());
        Assertions.assertEquals(0, count);
    }

    @Test
    void getCountFreePlaceInDate_TwoPlaceOneOrderWithMaster_CountOne() {
        Mockito.doReturn(new ArrayList<>(Arrays.asList(new Place("test1"), new Place("test2"))))
                .when(dateService).getFreePlaceInDate(any(LocalDateTime.class));
        Mockito.when(masterServiceMock.getListOfMasters(SortTypeMaster.ID))
                .thenReturn(new ArrayList<>(Arrays.asList(new Master("test1"), new Master("test2"))));
        ArrayList<Order> order = new ArrayList<>(Collections.singleton(
                new Order("test",
                        2,
                        new Place("test"),
                        LocalDateTime.now(),
                        LocalDateTime.of(2026, 3, 30, 12, 0),
                        LocalDateTime.of(2026, 3, 30, 15, 0)
                )
        ));
        Mockito.doReturn(order).when(dateService).getOrdersInTime(any(), any(), any(), any());
        Mockito.when(masterServiceMock.getMastersByOrder(any())).thenReturn(
                new ArrayList<>(Collections.singleton(new Master()))
        );
        int count = dateService.getCountFreePlaceInDate(LocalDateTime.of(2026, 3, 30, 13, 0));
        Mockito.verify(masterServiceMock).getMastersByOrder(any());
        Assertions.assertEquals(1, count);
    }

    @Test
    void getOrdersInTime_NullOrdersInTime_EmptyArrayList() {
        Mockito.when(orderServiceMock.getListOfOrders(any())).thenReturn(
                new ArrayList<>(Collections.singleton(
                        new Order("test",
                                2,
                                new Place("test"),
                                LocalDateTime.now(),
                                LocalDateTime.of(2026, 3, 30, 12, 0),
                                LocalDateTime.of(2026, 3, 30, 15, 0)
                        )
                ))
        );
        ArrayList<Order> orders = dateService.getOrdersInTime(
                StatusOrder.ACTIVE,
                LocalDateTime.of(2026, 3, 30, 16, 0),
                LocalDateTime.of(2026, 3, 30, 18, 0),
                SortTypeOrder.ID
        );
        Assertions.assertTrue(orders.isEmpty());
    }

    @Test
    void getOrdersInTime_OneOrdersInTime() {
        Mockito.when(orderServiceMock.getListOfOrders(any())).thenReturn(
                new ArrayList<>(Collections.singleton(
                        new Order("test",
                                2,
                                new Place("test"),
                                LocalDateTime.now(),
                                LocalDateTime.of(2026, 3, 30, 12, 0),
                                LocalDateTime.of(2026, 3, 30, 15, 0)
                        )
                ))
        );
        ArrayList<Order> orders = dateService.getOrdersInTime(
                StatusOrder.ACTIVE,
                LocalDateTime.of(2026, 3, 30, 14, 0),
                LocalDateTime.of(2026, 3, 30, 16, 0),
                SortTypeOrder.ID
        );
        Assertions.assertFalse(orders.isEmpty());
    }

    @Test
    void getFreePlaceInDate_NullOrders_OnePlace() {
        Mockito.when(placeServiceMock.getListOfPlace()).
                thenReturn(new ArrayList<>(Collections.singleton(new Place("test"))));
        Mockito.doReturn(new ArrayList<>()).when(dateService).getOrdersInTime(any(), any(), any(), any());
        ArrayList<Place> places = dateService.getFreePlaceInDate(LocalDateTime.now());
        Assertions.assertFalse(places.isEmpty());
    }

    @Test
    void getFreePlaceInDate_OneOrderWithIdPlace_EmptyArrayList() {
        Mockito.when(placeServiceMock.getListOfPlace()).
                thenReturn(new ArrayList<>(Collections.singleton(new Place(1, "test"))));
        Mockito.doReturn(new ArrayList<>(Collections.singleton(
                new Order("test",
                        2,
                        new Place(1, "test"),
                        LocalDateTime.now(),
                        LocalDateTime.of(2026, 3, 30, 12, 0),
                        LocalDateTime.of(2026, 3, 30, 15, 0)
                )
        )
        )).when(dateService).getOrdersInTime(any(), any(), any(), any());
        ArrayList<Place> places = dateService.getFreePlaceInDate(LocalDateTime.of(2026, 3, 30, 14, 0));
        Assertions.assertTrue(places.isEmpty());
    }
}
