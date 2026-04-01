package serviceTest;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.service.MasterService;
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

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MasterServiceTest {
    @InjectMocks
    MasterService masterService;
    @Mock
    MapperToEntity mapperToEntityMock;
    @Mock
    MasterRepository masterRepositoryMock;
    @Mock
    MapperToDto mapperToDtoMock;
    @Mock
    OrderRepository orderRepositoryMock;
    @Mock
    OrderMasterRepository orderMasterRepositoryMock;
    @Mock
    OrderMasterService orderMasterServiceMock;

    Master master;
    MasterRequest masterRequest;
    int id;

    @BeforeEach
    void setUp() {
        masterRequest = new MasterRequest("test");
        master = new Master(masterRequest.getName());
        id = 1;
    }

    @Test
    void addMaster_GoodAddMaster() {
        Mockito.when(mapperToEntityMock.mapToMasterEntity(masterRequest)).thenReturn(master);
        masterService.addMaster(masterRequest);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).create(master);
    }

    @Test
    void addMaster_BadMaster() {
        Mockito.when(mapperToEntityMock.mapToMasterEntity(masterRequest)).thenReturn(master);
        masterService.addMaster(masterRequest);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).create(master);
    }

    @Test
    void findMaster_RealMaster() {
        Mockito.when(masterRepositoryMock.find(id)).thenReturn(master);
        Mockito.when(mapperToDtoMock.mapToMasterDto(master)).thenReturn(new MasterDto());
        masterService.findMaster(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToMasterDto(master);
    }

    @Test
    void findMaster_NullMaster() {
        Mockito.when(masterRepositoryMock.find(id)).thenReturn(null);
        Mockito.when(mapperToDtoMock.mapToMasterDto(any())).thenReturn(new MasterDto());
        masterService.findMaster(id);
        Mockito.verify(mapperToDtoMock, Mockito.times(1)).mapToMasterDto(null);
    }

    @Test
    void updateMaster() {
        Master masterUpdate = new Master(id, masterRequest.getName());
        Mockito.when(mapperToEntityMock.mapToMasterEntity(id, masterRequest)).thenReturn(masterUpdate);
        Mockito.doNothing().when(masterRepositoryMock).update(masterUpdate);
        masterService.updateMaster(id, masterRequest);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).update(masterUpdate);
    }

    @Test
    void updateMaster_NoValidMaster() {
        Master masterUpdate = new Master(id, masterRequest.getName());
        Mockito.when(mapperToEntityMock.mapToMasterEntity(id, masterRequest)).thenReturn(masterUpdate);
        Mockito.doNothing().when(masterRepositoryMock).update(masterUpdate);
        masterService.updateMaster(id, masterRequest);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).update(masterUpdate);
    }

    @Test
    void deleteMaster() {
        Mockito.when(masterRepositoryMock.find(id)).thenReturn(master);
        Mockito.doNothing().when(orderMasterServiceMock).deleteByMaster(id);
        Mockito.when(masterRepositoryMock.delete(id)).thenReturn(true);
        masterService.deleteMaster(id);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).delete(id);
    }

    @Test
    void deleteMaster_masterNoFound() {
        Mockito.when(masterRepositoryMock.find(id)).thenReturn(null);
        masterService.deleteMaster(id);
        Mockito.verify(masterRepositoryMock, Mockito.times(0)).delete(id);
    }

    @Test
    void getAllMaster() {
        Mockito.when(masterRepositoryMock.findAll(SortTypeMaster.ID)).thenReturn(new ArrayList<>());
        masterService.getListOfMasters(SortTypeMaster.ID);
        Mockito.verify(masterRepositoryMock, Mockito.times(1)).findAll(SortTypeMaster.ID);
    }

    @Test
    void getMasterByOrder_RealOrder() {
        Order order = new Order();
        String name = "order_test_name";
        ArrayList<Master> verifyMasters = new ArrayList<>(Collections.singleton(master));
        ArrayList<OrderMaster> orderMasters = new ArrayList<>(Collections.singleton(new OrderMaster(order, master)));
        Mockito.when(orderRepositoryMock.find(name)).thenReturn(order);
        Mockito.when(orderMasterRepositoryMock.getMastersByOrderInDB(order.getId())).thenReturn(orderMasters);
        Mockito.when(orderMasterServiceMock.getMasterFromOrderMaster(orderMasters)).thenReturn(verifyMasters);
        ArrayList<Master> masters = masterService.getMastersByOrder(name);
        Assertions.assertEquals(verifyMasters.get(0), masters.get(0));
    }

    @Test
    void getMasterByOrder_NullOrder() {
        String name = "order_test_name";
        Mockito.when(orderRepositoryMock.find(name)).thenReturn(null);
        ArrayList<Master> nullMaster = masterService.getMastersByOrder(name);
        Assertions.assertTrue(nullMaster.isEmpty());
    }
}
