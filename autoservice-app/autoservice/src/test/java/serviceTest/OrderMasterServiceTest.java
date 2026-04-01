package serviceTest;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.Place;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.service.OrderMasterService;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OrderMasterServiceTest {
    @InjectMocks
    OrderMasterService orderMasterService;
    @Mock
    private MasterRepository masterRepositoryMock;
    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private OrderMasterRepository orderMasterRepositoryMock;
    @Mock
    private MapperToDto mapperToDtoMock;
    @Mock
    private MapperToEntity mapperToEntityMock;

    Master master;
    Order order;
    int id;
    OrderMaster orderMaster;

    @BeforeEach
    void setUp() {
        master = new Master(1, "test_Master");
        order = new Order(
                1,
                "test",
                2000,
                new Place("test_place"),
                LocalDateTime.now(),
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 15, 0)
        );
        id = 1;
        orderMaster = new OrderMaster(1, order, master);
    }

    @Test
    void addOrderMaster_WhenValidOrderAndMaster_ShouldCreate() {
        Mockito.when(masterRepositoryMock.find(master.getId())).thenReturn(master);
        Mockito.when(orderRepositoryMock.find(order.getId())).thenReturn(order);
        orderMasterService.addOrderMaster(master.getId(), order.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).create(any(OrderMaster.class));
    }

    @Test
    void addOrderMaster_WhenNullOrderAndMaster_ShouldThrowNullException() {
        Mockito.when(masterRepositoryMock.find(master.getId())).thenReturn(null);
        Mockito.when(orderRepositoryMock.find(order.getId())).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class,
                () -> orderMasterService.addOrderMaster(master.getId(), order.getId()));

    }

    @Test
    void getMasterFromOrderMaster_WhenValidOrderMaster_ShouldReturnArrayWithMaster() {
        ArrayList<Master> verifyMasters = new ArrayList<>(Collections.singleton(master));
        ArrayList<Master> masters = orderMasterService.getMasterFromOrderMaster(new ArrayList<>(Collections.singleton(orderMaster)));
        Assertions.assertEquals(verifyMasters, masters);
    }

    @Test
    void getMasterFromOrderMaster_WhenNullOrderMaster_ShouldReturnEmptyArray() {
        ArrayList<Master> masters = orderMasterService.getMasterFromOrderMaster(new ArrayList<>());
        Assertions.assertTrue(masters.isEmpty());
    }

    @Test
    void deleteByMaster_WhenMasterId_ShouldDelete() {
        orderMasterService.deleteByMaster(master.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).deleteByMaster(master.getId());
    }

    @Test
    void deleteByMaster_WhenOrderId_ShouldDelete() {
        orderMasterService.deleteByMaster(order.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).deleteByMaster(order.getId());
    }

    @Test
    void deleteByOrder_WhenOrderId_ShouldDelete() {
        orderMasterService.deleteByOrder(order.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).deleteByOrder(order.getId());
    }

    @Test
    void deleteByOrder_WhenMasterId_ShouldDelete() {
        orderMasterService.deleteByOrder(master.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).deleteByOrder(master.getId());
    }

    @Test
    void findOrderMaster_WhenFoundOrderMaster_ShouldReturnOrderMaster() {
        Mockito.when(orderMasterRepositoryMock.find(id)).thenReturn(orderMaster);
        Mockito.when(mapperToDtoMock.mapToOrderMasterDto(orderMaster)).thenReturn(new OrderMasterDto());
        orderMasterService.findOrderMaster(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToOrderMasterDto(orderMaster);
    }

    @Test
    void findOrderMaster_WhenNoFoundOrderMaster_ShouldReturnNull() {
        Mockito.when(orderMasterRepositoryMock.find(id)).thenReturn(null);
        Mockito.when(mapperToDtoMock.mapToOrderMasterDto(null)).thenReturn(new OrderMasterDto());
        orderMasterService.findOrderMaster(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToOrderMasterDto(null);
    }

    @Test
    void updateOrderMaster_WhenValidOrderMaster_ShouldUpdate() {
        Mockito.when(mapperToEntityMock.mapToOrderMasterEntity(any(Integer.class), any(OrderDto.class), any(MasterDto.class))).thenReturn(orderMaster);
        orderMasterService.updateOrderMaster(id, new OrderDto(), new MasterDto());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).update(orderMaster);
    }

    @Test
    void updateOrderMaster_WhenNullOrderMaster_ShouldUpdateOrderMaster() {
        OrderMaster nullOrderMaster = new OrderMaster();
        Mockito.when(mapperToEntityMock.mapToOrderMasterEntity(any(Integer.class), any(OrderDto.class), any(MasterDto.class)))
                .thenReturn(nullOrderMaster);
        orderMasterService.updateOrderMaster(id, new OrderDto(), new MasterDto());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).update(nullOrderMaster);
    }

    @Test
    void getOrderMasterByMaster_WhenMasterId_ShouldFound() {
        orderMasterService.getOrderMasterDtoByMaster(master.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).getOrdersByMasterInDB(master.getId());
    }

    @Test
    void getOrderMasterByMaster_WhenOrderId_ShouldFound() {
        orderMasterService.getOrderMasterDtoByMaster(order.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).getOrdersByMasterInDB(order.getId());
    }

    @Test
    void getOrderMasterByOrder_WhenMasterId_ShouldFound() {
        orderMasterService.getOrderMasterDtoByOrder(master.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).getMastersByOrderInDB(master.getId());
    }

    @Test
    void getOrderMasterByOrder_orderId_ShouldFound() {
        orderMasterService.getOrderMasterDtoByOrder(order.getId());
        Mockito.verify(orderMasterRepositoryMock, Mockito.times(1)).getMastersByOrderInDB(order.getId());
    }
}
