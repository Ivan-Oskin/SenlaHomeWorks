package controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oskin.autoservice.controller.UserController;
import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.exception.GlobalExceptionHandler;
import com.oskin.autoservice.security.UserDetailService;
import com.oskin.autoservice.service.UserService;
import com.oskin.autoservice.utils.JwtUtils;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    PasswordEncoder passwordEncoderMock;
    @Mock
    AuthenticationManager authenticationManagerMock;
    @Mock
    UserDetailService userDetailServiceMock;
    @Mock
    JwtUtils jwtUtilsMock;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        captor = ArgumentCaptor.forClass(UserRequest.class);
    }

    @Test
    void saveUser_WhenValidUser_ShouldSaveUserWithHashedPassword() throws Exception {
        UserRequest userRequest = new UserRequest("test", "password");
        Mockito.when(passwordEncoderMock.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        mockMvc.perform(post("/autoservice/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
        Mockito.verify(userServiceMock, Mockito.times(1)).createUser(captor.capture());

        UserRequest captured = captor.getValue();
        Assertions.assertEquals("encodedPassword", captured.getPassword());
    }

    @Test
    void saveUser_WhenInvalidJson_ShouldReturnBadRequest() throws Exception {
        String invalidJson = "{\"password\":\"password\"}";
        mockMvc.perform(post("/autoservice/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(userServiceMock, Mockito.times(0)).createUser(any());
    }

    @Test
    void auth_WhenGetValidToken_ShouldGenerateToken() throws Exception {
        UserRequest userRequest = new UserRequest("test", "password");
        UserDetails userDetails = User.builder()
                .username(userRequest.getLogin())
                .password(userRequest.getPassword()).build();
        Mockito.when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(userDetailServiceMock.loadUserByUsername(userRequest.getLogin())).thenReturn(userDetails);
        Mockito.when(jwtUtilsMock.generateToken(userDetails)).thenReturn("token");

        mockMvc.perform(post("/autoservice/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
        Mockito.verify(jwtUtilsMock, Mockito.times(1)).generateToken(userDetails);
    }

    @Test
    void auth_WhenNoFoundUser_ShouldNoGeneratedToken() throws Exception {
        UserRequest userRequest = new UserRequest("test", "password");
        UserDetails userDetails = User.builder()
                .username(userRequest.getLogin())
                .password(userRequest.getPassword()).build();
        Mockito.when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(userDetailServiceMock.loadUserByUsername(userRequest.getLogin()))
                .thenThrow(new UsernameNotFoundException("user no found"));

        mockMvc.perform(post("/autoservice/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isUnprocessableContent());
        Mockito.verify(jwtUtilsMock, Mockito.times(0)).generateToken(userDetails);
    }
}
