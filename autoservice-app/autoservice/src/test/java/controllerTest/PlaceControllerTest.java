package controllerTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.autoservice.controller.PlaceController;
import com.oskin.autoservice.dto.DateDto;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.service.DateService;
import com.oskin.autoservice.service.PlaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
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
public class PlaceControllerTest {
    @InjectMocks
    private PlaceController placeController;
    @Mock
    private PlaceService placeServiceMock;
    @Mock
    private DateService dateServiceMock;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private ArgumentCaptor<PlaceRequest> captor;
    private int placeId;
    private PlaceDto placeDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mockMvc = MockMvcBuilders.standaloneSetup(placeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        captor = ArgumentCaptor.forClass(PlaceRequest.class);
        placeId = 1;
        placeDto = new PlaceDto();
        placeDto.setName("test");
        placeDto.setId(1);
    }

    @Test
    void findAllTest() throws Exception {
        mockMvc.perform(get("/autoservice/places"))
                .andExpect(status().isOk());
        Mockito.verify(placeServiceMock, Mockito.times(1)).getListOfPlaceDto();
    }

    @Test
    void findById_WhenValidId_ShouldReturnPlaceDto() throws Exception {
        mockMvc.perform(get("/autoservice/places/{id}", placeId))
                .andExpect(status().isOk());
        Mockito.verify(placeServiceMock, Mockito.times(1)).findPlace(placeId);
    }

    @Test
    void findById_WhenNoFoundById_ShouldThrowNullException() throws Exception {
        Mockito.when(placeServiceMock.findPlace(placeId)).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/places/{id}", placeId))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(placeServiceMock, Mockito.times(1)).findPlace(placeId);
    }

    @Test
    void getFreePlace_WhenValidDateDto_ShouldReturnArrayPlaceDto() throws Exception {
        DateDto dateDto = new DateDto();
        dateDto.setDate(LocalDateTime.of(2026, 1, 1, 12, 0));
        Mockito.when(dateServiceMock.getFreePlaceDto(dateDto.getDate()))
                .thenReturn(new ArrayList<>(Collections.singleton(placeDto)));
        MvcResult result = mockMvc.perform(get("/autoservice/places/free")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponce = result.getResponse().getContentAsString();
        ArrayList<PlaceDto> arrayPlaceDto = objectMapper.readValue(jsonResponce,
                new TypeReference<ArrayList<PlaceDto>>() {
                });
        Mockito.verify(dateServiceMock, Mockito.times(1)).getFreePlaceDto(dateDto.getDate());
        Assertions.assertFalse(arrayPlaceDto.isEmpty());
    }

    @Test
    void getFreePlace_WhenNoValidDateDto_ShouldThrowException() throws Exception {
        DateDto dateDto = new DateDto();
        Mockito.when(dateServiceMock.getFreePlaceDto(dateDto.getDate()))
                .thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/places/free")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateDto)))
                .andExpect(status().isUnprocessableContent());

        Mockito.verify(dateServiceMock, Mockito.times(1)).getFreePlaceDto(dateDto.getDate());
    }

    @Test
    void getCountFreePlace_WhenValidDateDto_ShouldReturnCount() throws Exception {
        DateDto dateDto = new DateDto();
        dateDto.setDate(LocalDateTime.of(2026, 1, 1, 12, 0));
        Mockito.when(dateServiceMock.getCountFreePlaceInDate(dateDto.getDate()))
                .thenReturn(1);
        MvcResult result = mockMvc.perform(get("/autoservice/places/free/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponce = result.getResponse().getContentAsString();
        Integer count = objectMapper.readValue(jsonResponce, new TypeReference<Integer>() {
        });
        Mockito.verify(dateServiceMock, Mockito.times(1)).getCountFreePlaceInDate(dateDto.getDate());
        Assertions.assertEquals(1, count);
    }

    @Test
    void getCountFreePlace_WhenNoValidDateDto_ShouldThrowException() throws Exception {
        DateDto dateDto = new DateDto();
        Mockito.when(dateServiceMock.getCountFreePlaceInDate(dateDto.getDate()))
                .thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/places/free/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateDto)))
                .andExpect(status().isUnprocessableContent());

        Mockito.verify(dateServiceMock, Mockito.times(1)).getCountFreePlaceInDate(dateDto.getDate());
    }

    @Test
    void savePlace_WhenValidPlaceRequest_ShouldSavePlace() throws Exception {
        PlaceRequest placeRequest = new PlaceRequest();
        placeRequest.setName("test");
        mockMvc.perform(post("/autoservice/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest))).
                andExpect(status().isOk());

        Mockito.verify(placeServiceMock, Mockito.times(1)).addPlace(any(PlaceRequest.class));
    }

    @Test
    void savePlace_WhenNoValidPlaceRequest_ShouldNotThrowException() throws Exception {
        mockMvc.perform(post("/autoservice/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"admin\"}")).
                andExpect(status().isOk());

        Mockito.verify(placeServiceMock, Mockito.times(1)).addPlace(any(PlaceRequest.class));
    }

    @Test
    void updatePlace_WhenValidPlaceRequest_ShouldUpdate() throws Exception {
        PlaceRequest placeRequest = new PlaceRequest();
        placeRequest.setName("test");
        mockMvc.perform(put("/autoservice/places/{id}", placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(status().isOk());
        Mockito.verify(placeServiceMock, Mockito.times(1))
                .updatePlace(any(Integer.class), any(PlaceRequest.class));
    }

    @Test
    void updatePlace_WhenNoValidPlaceRequest_ShouldTrowException() throws Exception {
        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(placeServiceMock).updatePlace(any(Integer.class), any(PlaceRequest.class));
        PlaceRequest placeRequest = new PlaceRequest();
        placeRequest.setName("test");
        mockMvc.perform(put("/autoservice/places/{id}", placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeRequest)))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(placeServiceMock, Mockito.times(1))
                .updatePlace(any(Integer.class), any(PlaceRequest.class));
    }

    @Test
    void deletePlace_WhenValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/places/{id}", placeId)).andExpect(status().isOk());
        Mockito.verify(placeServiceMock, Mockito.times(1)).deletePlace(placeId);
    }

    @Test
    void deletePlace_WhenNoValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/places/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(placeServiceMock, Mockito.times(1)).deletePlace(-25);
    }
}
