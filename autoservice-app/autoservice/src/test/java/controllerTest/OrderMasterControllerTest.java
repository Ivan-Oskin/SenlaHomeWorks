package controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.autoservice.controller.OrderMasterController;
import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.OrderMasterRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.MasterService;
import com.oskin.autoservice.service.OrderMasterService;
import com.oskin.autoservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class OrderMasterControllerTest {
    @InjectMocks
    OrderMasterController orderMasterController;
    @Mock
    public OrderMasterService orderMasterServiceMock;
    @Mock
    public OrderService orderServiceMock;
    @Mock
    public MasterService masterServiceMock;
    @Mock
    public DateService dateServiceMock;

    ObjectMapper objectMapper;
    MockMvc mockMvc;
    int orderMasterId;
    int orderId;
    int masterId;
    OrderMasterDto orderMasterDto;
    OrderMasterRequest orderMasterRequest;
    OrderDto orderDto;
    MasterDto masterDto;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(orderMasterController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        orderMasterId = 1;
        orderId = 1;
        masterId = 1;

        PlaceDto placeDto;
        placeDto = new PlaceDto();
        placeDto.setId(1);
        placeDto.setName("test");

        orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setName("test");
        orderDto.setCost(2000);
        orderDto.setTimeCreate(LocalDateTime.of(2026, 1, 1, 12, 0));
        orderDto.setTimeStart(LocalDateTime.of(2026, 1, 1, 14, 0));
        orderDto.setTimeComplete(LocalDateTime.of(2026, 1, 1, 15, 0));
        orderDto.setStatus(StatusOrder.ACTIVE);
        orderDto.setPlaceDto(placeDto);

        masterDto = new MasterDto();
        masterDto.setId(1);
        masterDto.setName("test");

        orderMasterDto = new OrderMasterDto();
        orderMasterDto.setId(1);
        orderMasterDto.setOrderDto(orderDto);
        orderMasterDto.setMasterDto(masterDto);

        orderMasterRequest = new OrderMasterRequest(1, 1);
    }

    @Test
    void findAllTest() throws Exception {
        mockMvc.perform(get("/autoservice/order_master"))
                .andExpect(status().isOk());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).getListOfOrderMasterDto();
    }

    @Test
    void getNearestDateTest() throws Exception {
        mockMvc.perform(get("/autoservice/order_master/nearest_date")).andExpect(status().isOk());
        Mockito.verify(dateServiceMock, Mockito.times(1)).getNearestDate();
    }

    @Test
    void findById_WhenValidId_ShouldReturnOrderMasterDto() throws Exception {
        mockMvc.perform(get("/autoservice/order_master/{id}", orderMasterId))
                .andExpect(status().isOk());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).findOrderMaster(orderMasterId);
    }

    @Test
    void findById_WhenNoFoundById_ShouldThrowNullException() throws Exception {
        Mockito.when(orderMasterServiceMock.findOrderMaster(orderMasterId)).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/order_master/{id}", orderMasterId))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).findOrderMaster(orderMasterId);
    }

    @Test
    void findByMasterId_WhenValidId_ShouldReturnListOrderMasterDto() throws Exception {
        ArrayList<OrderMasterDto> verifyOrderMaster = new ArrayList<>(Collections.singleton(orderMasterDto));
        Mockito.when(orderMasterServiceMock.getOrderMasterDtoByMaster(masterId)).thenReturn(verifyOrderMaster);
        MvcResult result = mockMvc.perform(get("/autoservice/order_master/master/{id}", masterId))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ArrayList<OrderMasterDto> orderMasterDtoArrayList = objectMapper
                .readValue(response, new TypeReference<ArrayList<OrderMasterDto>>() {
                });
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).getOrderMasterDtoByMaster(masterId);
        Assertions.assertEquals(verifyOrderMaster.get(0).getId(), orderMasterDtoArrayList.get(0).getId());
    }

    @Test
    void findByMasterId_WhenNoValidId_ShouldReturnEmptyListOrderMasterDto() throws Exception {
        Mockito.when(orderMasterServiceMock.getOrderMasterDtoByMaster(masterId)).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/autoservice/order_master/master/{id}", masterId))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ArrayList<OrderMasterDto> orderMasterDtoArrayList = objectMapper
                .readValue(response, new TypeReference<ArrayList<OrderMasterDto>>() {
                });
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).getOrderMasterDtoByMaster(masterId);
        Assertions.assertTrue(orderMasterDtoArrayList.isEmpty());
    }

    @Test
    void findByOrderId_WhenValidId_ShouldReturnListOrderMasterDto() throws Exception {
        ArrayList<OrderMasterDto> verifyOrderMaster = new ArrayList<>(Collections.singleton(orderMasterDto));
        Mockito.when(orderMasterServiceMock.getOrderMasterDtoByOrder(orderId)).thenReturn(verifyOrderMaster);
        MvcResult result = mockMvc.perform(get("/autoservice/order_master/order/{id}", orderId))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ArrayList<OrderMasterDto> orderMasterDtoArrayList = objectMapper
                .readValue(response, new TypeReference<ArrayList<OrderMasterDto>>() {
                });
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).getOrderMasterDtoByOrder(orderId);
        Assertions.assertEquals(verifyOrderMaster.get(0).getId(), orderMasterDtoArrayList.get(0).getId());
    }

    @Test
    void findByOrderId_WhenNoValidId_ShouldReturnEmptyListOrderMasterDto() throws Exception {
        Mockito.when(orderMasterServiceMock.getOrderMasterDtoByOrder(orderId)).thenReturn(new ArrayList<>());
        MvcResult result = mockMvc.perform(get("/autoservice/order_master/order/{id}", orderId))
                .andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString();
        ArrayList<OrderMasterDto> orderMasterDtoArrayList = objectMapper
                .readValue(response, new TypeReference<ArrayList<OrderMasterDto>>() {
                });
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).getOrderMasterDtoByOrder(orderId);
        Assertions.assertTrue(orderMasterDtoArrayList.isEmpty());
    }

    @Test
    void saveOrderMaster_WhenValidOrderMasterRequest_ShouldSaveOrderMaster() throws Exception {
        mockMvc.perform(post("/autoservice/order_master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderMasterRequest))).
                andExpect(status().isOk());

        Mockito.verify(orderMasterServiceMock, Mockito.times(1))
                .addOrderMaster(any(Integer.class), any(Integer.class));
    }

    @Test
    void saveOrderMaster_WhenNoValidOrderMasterRequest_ShouldThrowException() throws Exception {
        Mockito.doThrow(NullPointerException.class)
                .when(orderMasterServiceMock).addOrderMaster(any(Integer.class), any(Integer.class));
        mockMvc.perform(post("/autoservice/order_master")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"admin\"}")).
                andExpect(status().isUnprocessableContent());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).addOrderMaster(any(Integer.class), any(Integer.class));
    }

    @Test
    void updateOrderMaster_WhenValidRequest_ShouldUpdate() throws Exception {
        Mockito.when(orderServiceMock.findOrder(orderId)).thenReturn(orderDto);
        Mockito.when(masterServiceMock.findMaster(masterId)).thenReturn(masterDto);
        mockMvc.perform(put("/autoservice/order_master/{id}", orderMasterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderMasterRequest))).andExpect(status().isOk());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1))
                .updateOrderMaster(any(Integer.class), any(OrderDto.class), any(MasterDto.class));
    }

    @Test
    void updateOrderMaster_WhenNoFoundOrderAndMaster_ShouldThrowException() throws Exception {
        Mockito.when(orderServiceMock.findOrder(orderId)).thenReturn(null);
        Mockito.when(masterServiceMock.findMaster(masterId)).thenReturn(null);
        Mockito.doThrow(NullPointerException.class).when(orderMasterServiceMock)
                .updateOrderMaster(orderMasterId, null, null);
        mockMvc.perform(put("/autoservice/order_master/{id}", orderMasterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderMasterRequest))).andExpect(status().isUnprocessableContent());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1))
                .updateOrderMaster(any(Integer.class), any(), any());
    }

    @Test
    void deleteOrderMaster_WhenValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/order_master/{id}", orderMasterId)).andExpect(status().isOk());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).delete(orderMasterId);
    }

    @Test
    void deleteOrderMaster_WhenNoValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/order_master/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(orderMasterServiceMock, Mockito.times(1)).delete(-25);
    }
}
