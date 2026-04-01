package controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.autoservice.controller.UserController;
import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.service.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    private ArgumentCaptor<UserRequest> captor;
    ObjectMapper objectMapper;
    @Mock
    UserService userServiceMock;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        captor = ArgumentCaptor.forClass(UserRequest.class);
    }

    @Test
    void save_createUser() throws Exception {
        UserRequest userRequest = new UserRequest("test", "password");
        Mockito.when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        mockMvc.perform(post("/autoservice/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
        Mockito.verify(userServiceMock, Mockito.times(1)).createUser(captor.capture());

        UserRequest captured = captor.getValue();
        Assertions.assertEquals("encodedPassword", captured.getPassword());
    }

    @Test
    void save_InvalidJson_ReturnBadRequest() throws Exception {
        String invalidJson = "{\"password\":\"password\"}";
        mockMvc.perform(post("/autoservice/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(userServiceMock, Mockito.times(0)).createUser(any());
    }
}
