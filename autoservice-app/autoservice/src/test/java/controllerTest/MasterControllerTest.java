package controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.autoservice.controller.MasterController;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.service.MasterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class MasterControllerTest {
    @InjectMocks
    MasterController masterController;
    @Mock
    MasterService masterServiceMock;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private int masterId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(masterController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        masterId = 1;
    }

    @Test
    void findAllTest() throws Exception {
        mockMvc.perform(get("/autoservice/masters"))
                .andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).getListOfMastersDto(SortTypeMaster.ID);
    }

    @Test
    void findAllSortByBusyTest() throws Exception {
        mockMvc.perform(get("/autoservice/masters/sort_by_busy"))
                .andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).getListOfMastersDto(SortTypeMaster.BUSYNESS);
    }

    @Test
    void findAllSortByNameTest() throws Exception {
        mockMvc.perform(get("/autoservice/masters/sort_by_name"))
                .andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).getListOfMastersDto(SortTypeMaster.NAME);
    }

    @Test
    void findById_WhenValidId_ShouldReturnMasterDto() throws Exception {
        mockMvc.perform(get("/autoservice/masters/{id}", masterId))
                .andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).findMaster(masterId);
    }

    @Test
    void findById_WhenNoFoundById_ShouldThrowNullException() throws Exception {
        Mockito.when(masterServiceMock.findMaster(masterId)).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/autoservice/masters/{id}", masterId))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(masterServiceMock, Mockito.times(1)).findMaster(masterId);
    }

    @Test
    void saveMaster_WhenValidMasterRequest_ShouldSaveMaster() throws Exception {
        MasterRequest masterRequest = new MasterRequest();
        masterRequest.setName("test");
        mockMvc.perform(post("/autoservice/masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(masterRequest))).
                andExpect(status().isOk());

        Mockito.verify(masterServiceMock, Mockito.times(1)).addMaster(any(MasterRequest.class));
    }

    @Test
    void saveMaster_WhenNoValidMasterRequest_ShouldNotThrowException() throws Exception {
        mockMvc.perform(post("/autoservice/masters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"admin\"}")).
                andExpect(status().isOk());

        Mockito.verify(masterServiceMock, Mockito.times(1)).addMaster(any(MasterRequest.class));
    }

    @Test
    void updateMaster_WhenValidMasterRequest_ShouldUpdate() throws Exception {
        MasterRequest masterRequest = new MasterRequest();
        masterRequest.setName("test");
        mockMvc.perform(put("/autoservice/masters/{id}", masterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(masterRequest)))
                .andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1))
                .updateMaster(any(Integer.class), any(MasterRequest.class));
    }

    @Test
    void updateMaster_WhenNoValidMasterRequest_ShouldTrowException() throws Exception {
        MasterRequest masterRequest = new MasterRequest();
        masterRequest.setName("test");
        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(masterServiceMock).updateMaster(any(Integer.class), any(MasterRequest.class));
        mockMvc.perform(put("/autoservice/masters/{id}", masterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(masterRequest)))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(masterServiceMock, Mockito.times(1))
                .updateMaster(any(Integer.class), any(MasterRequest.class));
    }

    @Test
    void deleteMaster_WhenValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/masters/{id}", masterId)).andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).deleteMaster(masterId);
    }

    @Test
    void deleteMaster_WhenNoValidId_ShouldDelete() throws Exception {
        mockMvc.perform(delete("/autoservice/masters/{id}", -25)).andExpect(status().isOk());
        Mockito.verify(masterServiceMock, Mockito.times(1)).deleteMaster(-25);
    }
}
