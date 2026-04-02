package controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.autoservice.controller.OrderController;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.OffsetRequest;
import com.oskin.autoservice.dto.request.OrderRequest;
import com.oskin.autoservice.dto.request.TwoDateRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.model.SortTypeOrder;
import com.oskin.autoservice.model.StatusOrder;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.OrderService;
import com.oskin.autoservice.service.PlaceService;
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
public class OrderControllerTest {
    @InjectMocks
    OrderController orderController;
    @Mock
    private OrderService orderServiceMock;
    @Mock
    private PlaceService placeServiceMock;
    @Mock
    private DateService dateServiceMock;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private int orderId;
    private OrderRequest orderRequest;
    private OrderDto orderDto;
    private PlaceDto placeDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        orderId = 1;
        orderRequest = new OrderRequest("test",
                2000,
                LocalDateTime.of(2026, 1, 1, 12, 0),
                LocalDateTime.of(2026, 1, 1, 15, 0),
                1);
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
    }

    @Test
    void findAllTest() throws Exception {
        mockMvc.perform(get("/autoservice/orders"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfOrdersDto(SortTypeOrder.ID);
    }

    @Test
    void findAllTestByTimeCreate() throws Exception {
        mockMvc.perform(get("/autoservice/orders/sort_by_time_create"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfOrdersDto(SortTypeOrder.CREATE);
    }

    @Test
    void findAllTestByTimeStart() throws Exception {
        mockMvc.perform(get("/autoservice/orders/sort_by_time_start"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfOrdersDto(SortTypeOrder.START);
    }

    @Test
    void findAllTestByTimeComplete() throws Exception {
        mockMvc.perform(get("/autoservice/orders/sort_by_time_complete"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfOrdersDto(SortTypeOrder.COMPLETE);
    }

    @Test
    void findAllTestByCost() throws Exception {
        mockMvc.perform(get("/autoservice/orders/sort_by_cost"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfOrdersDto(SortTypeOrder.COST);
    }

    @Test
    void findActiveOrderTestByTimeCreate() throws Exception {
        mockMvc.perform(get("/autoservice/orders/active/sort_by_time_create"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfActiveOrders(SortTypeOrder.CREATE);
    }

    @Test
    void findActiveOrderTestByTimeComplete() throws Exception {
        mockMvc.perform(get("/autoservice/orders/active/sort_by_time_complete"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfActiveOrders(SortTypeOrder.COMPLETE);
    }

    @Test
    void findActiveOrderTestByCost() throws Exception {
        mockMvc.perform(get("/autoservice/orders/active/sort_by_cost"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).getListOfActiveOrders(SortTypeOrder.COST);
    }

    @Test
    void findById_WhenValidId_ShouldReturnOrderDto() throws Exception {
        mockMvc.perform(get("/autoservice/orders/{id}", orderId))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).findOrder(orderId);
    }

    @Test
    void findById_WhenNoFoundById_ShouldThrowNullException() throws Exception {
        Mockito.when(orderServiceMock.findOrder(orderId)).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/orders/{id}", orderId))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(orderServiceMock, Mockito.times(1)).findOrder(orderId);
    }

    @Test
    void saveOrder_WhenFoundPlace_ShouldSaveOrder() throws Exception {
        Mockito.when(placeServiceMock.findPlace(orderRequest.getPlaceId())).thenReturn(placeDto);
        mockMvc.perform(post("/autoservice/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))).
                andExpect(status().isOk());

        Mockito.verify(orderServiceMock, Mockito.times(1))
                .addOrder(any(OrderRequest.class), any(PlaceDto.class));
    }

    @Test
    void saveOrder_WhenNoFoundPlace_ShouldSaveOrder() throws Exception {
        Mockito.when(placeServiceMock.findPlace(orderRequest.getPlaceId())).thenReturn(null);
        mockMvc.perform(post("/autoservice/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))).
                andExpect(status().isOk());

        Mockito.verify(orderServiceMock, Mockito.times(0))
                .addOrder(any(OrderRequest.class), any(PlaceDto.class));
    }

    @Test
    void updateOrder_WhenFoundPlace_ShouldUpdate() throws Exception {
        Mockito.when(placeServiceMock.findPlace(orderRequest.getPlaceId())).thenReturn(placeDto);
        mockMvc.perform(put("/autoservice/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1))
                .updateOrder(any(Integer.class), any(OrderRequest.class), any(PlaceDto.class));
    }

    @Test
    void updateOrder_WhenNoFoundPlace_ShouldUpdate() throws Exception {
        Mockito.when(placeServiceMock.findPlace(orderRequest.getPlaceId())).thenReturn(null);
        mockMvc.perform(put("/autoservice/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(0))
                .updateOrder(any(Integer.class), any(OrderRequest.class), any(PlaceDto.class));
    }

    @Test
    void deleteOrder_WhenValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/orders/{id}", orderId)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).deleteOrder(orderId);
    }

    @Test
    void deleteOrder_WhenNoValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/orders/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).deleteOrder(-25);
    }

    @Test
    void closeOrder_WhenValidId_ShouldClose() throws Exception {
        mockMvc.perform(put("/autoservice/orders/close/{id}", orderId)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).closeOrder(orderId);
    }

    @Test
    void closeOrder_WhenNoValidId_ShouldClose() throws Exception {
        mockMvc.perform(put("/autoservice/orders/close/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).closeOrder(-25);
    }

    @Test
    void cancelOrder_WhenValidId_ShouldCancel() throws Exception {
        mockMvc.perform(put("/autoservice/orders/cancel/{id}", orderId)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).cancelOrder(orderId);
    }

    @Test
    void cancelOrder_WhenNoValidId_ShouldCancel() throws Exception {
        mockMvc.perform(put("/autoservice/orders/cancel/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).cancelOrder(-25);
    }

    @Test
    void activateOrder_WhenValidId_ShouldActivate() throws Exception {
        mockMvc.perform(put("/autoservice/orders/activate/{id}", orderId)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).activateOrder(orderId);
    }

    @Test
    void activateOrder_WhenNoValidId_ShouldActivate() throws Exception {
        mockMvc.perform(put("/autoservice/orders/activate/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1)).activateOrder(-25);
    }

    @Test
    void getOrdersInTime_WhenValidUrl_ShouldReturnList() throws Exception {
        ArrayList<OrderDto> VerifyOrderDto = new ArrayList<>(Collections.singleton(orderDto));
        Mockito.when(dateServiceMock
                        .getOrdersInTime(any(StatusOrder.class), any(TwoDateRequest.class), any(SortTypeOrder.class)))
                .thenReturn(VerifyOrderDto);
        MvcResult result = mockMvc.perform(get("/autoservice/orders/in_time/{status}/{sortType}",
                        "active", "sort_by_cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TwoDateRequest())))
                .andExpect(status().isOk()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ArrayList<OrderDto> ArrayOrderDto = objectMapper.
                readValue(jsonResponse, new TypeReference<ArrayList<OrderDto>>() {
                });
        Assertions.assertEquals(VerifyOrderDto.get(0).getId(), ArrayOrderDto.get(0).getId());
    }

    @Test
    void getOrdersInTime_WhenNoValidSortTypeAndStatus_ShouldReturnList() throws Exception {
        ArrayList<OrderDto> VerifyOrderDto = new ArrayList<>(Collections.singleton(orderDto));
        Mockito.when(dateServiceMock
                        .getOrdersInTime(any(StatusOrder.class), any(TwoDateRequest.class), any(SortTypeOrder.class)))
                .thenReturn(VerifyOrderDto);
        MvcResult result = mockMvc.perform(get("/autoservice/orders/in_time/{status}/{sortType}",
                        "lklk", "jlkjlkjkljlkjlkj")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TwoDateRequest())))
                .andExpect(status().isOk()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ArrayList<OrderDto> ArrayOrderDto = objectMapper.
                readValue(jsonResponse, new TypeReference<ArrayList<OrderDto>>() {
                });
        Assertions.assertEquals(VerifyOrderDto.get(0).getId(), ArrayOrderDto.get(0).getId());
    }

    @Test
    void offsetOrder_WhenValidRequest_ShouldOffset() throws Exception {
        OffsetRequest offsetRequest = new OffsetRequest(1, 1);
        mockMvc.perform(post("/autoservice/orders/offset/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offsetRequest)))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1))
                .offset(any(Integer.class), any(OffsetRequest.class));
    }

    @Test
    void offsetOrder_WhenNoValidRequest_ShouldOffset() throws Exception {
        mockMvc.perform(post("/autoservice/orders/offset/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"1234\"}"))
                .andExpect(status().isOk());
        Mockito.verify(orderServiceMock, Mockito.times(1))
                .offset(any(Integer.class), any(OffsetRequest.class));
    }
}
