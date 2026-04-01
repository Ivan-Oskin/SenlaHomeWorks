package serviceTest;

import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.OffsetRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.model.*;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.service.OrderMasterService;
import com.oskin.autoservice.service.OrderService;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Spy
    @InjectMocks
    OrderService orderService;
    @Mock
    MapperToEntity mapperToEntityMock;
    @Mock
    OrderRepository orderRepositoryMock;
    @Mock
    MapperToDto mapperToDtoMock;
    @Mock
    OrderMasterRepository orderMasterRepositoryMock;
    @Mock
    OrderMasterService orderMasterServiceMock;

    Order order;
    OrderRequest orderRequest;
    int id;
    Place place;
    PlaceDto placeDto;

    @BeforeEach
    void setUp() {
        placeDto = new PlaceDto();
        placeDto.setName("test");
        place = new Place(1, placeDto.getName());
        orderRequest = new OrderRequest(
                "test",
                2000,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 15, 0),
                1);
        order = new Order(
                1,
                orderRequest.getName(),
                orderRequest.getCost(),
                place,
                LocalDateTime.now(),
                orderRequest.getTimeStart(),
                orderRequest.getTimeComplete()
        );
        id = 1;
    }

    @Test
    void addOrder_GoodAddOrder() {
        Mockito.when(mapperToEntityMock.mapToOrderEntity(any(OrderRequest.class), any(Place.class))).thenReturn(order);
        orderService.addOrder(orderRequest, placeDto);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).create(order);
    }

    @Test
    void addOrder_NullPlace() {
        Mockito.when(mapperToEntityMock.mapToOrderEntity(any(OrderRequest.class), any(Place.class))).thenReturn(order);
        orderService.addOrder(orderRequest, new PlaceDto());
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).create(order);
    }

    @Test
    void findOrder_RealOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(order);
        Mockito.when(mapperToDtoMock.mapToOrderDto(order)).thenReturn(new OrderDto());
        orderService.findOrder(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToOrderDto(order);
    }

    @Test
    void findOrder_NullOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(null);
        Mockito.when(mapperToDtoMock.mapToOrderDto(any())).thenReturn(new OrderDto());
        orderService.findOrder(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToOrderDto(null);
    }

    @Test
    void updateOrder_NullOrderDto() {
        Mockito.when(orderService.findOrder(id)).thenReturn(new OrderDto());
        orderService.updateOrder(id, orderRequest, placeDto);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).update(any(Order.class));
    }

    @Test
    void updateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(id);
        orderDto.setCost(2000);
        orderDto.setStatus(StatusOrder.ACTIVE);
        orderDto.setTimeStart(order.getTimeStart());
        orderDto.setTimeCreate(order.getTimeCreate());
        orderDto.setTimeComplete(order.getTimeComplete());
        orderDto.setPlaceDto(placeDto);
        Mockito.when(orderService.findOrder(id)).thenReturn(orderDto);
        orderService.updateOrder(id, orderRequest, placeDto);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).update(any(Order.class));
    }

    @Test
    void deleteOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(order);
        Mockito.when(orderRepositoryMock.delete(id)).thenReturn(true);
        orderService.deleteOrder(id);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).delete(id);
    }

    @Test
    void deleteOrder_nullOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(null);
        orderService.deleteOrder(id);
        Mockito.verify(orderRepositoryMock, Mockito.times(0)).delete(id);
    }

    @Test
    void closeOrder() {
        orderService.closeOrder(id);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).changeStatusInDb(id, StatusOrder.CLOSE);
    }

    @Test
    void cancelOrder() {
        orderService.cancelOrder(id);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).changeStatusInDb(id, StatusOrder.CANCEL);
    }

    @Test
    void activateOrder() {
        orderService.activateOrder(id);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).changeStatusInDb(id, StatusOrder.ACTIVE);
    }

    @Test
    void offsetOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(order);
        orderService.offset(id, new OffsetRequest(1,1));
        Mockito.verify(orderRepositoryMock, Mockito.times(1))
                .offsetInDb(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void offsetOrder_nullOrder() {
        Mockito.when(orderRepositoryMock.find(id)).thenReturn(null);
        orderService.offset(id, new OffsetRequest(1,1));
        Mockito.verify(orderRepositoryMock, Mockito.times(0))
                .offsetInDb(any(Integer.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getAllOrders() {
        Mockito.when(orderRepositoryMock.findAll(SortTypeOrder.ID)).thenReturn(new ArrayList<>());
        orderService.getListOfOrders(SortTypeOrder.ID);
        Mockito.verify(orderRepositoryMock, Mockito.times(1)).findAll(SortTypeOrder.ID);
    }
}
