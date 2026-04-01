package serviceTest;

import com.oskin.autoservice.dto.request.UserRequest;
import com.oskin.autoservice.exception.UserAlreadyExistsException;
import com.oskin.autoservice.model.User;
import com.oskin.autoservice.model.UserRole;
import com.oskin.autoservice.repository.UserRepository;
import com.oskin.autoservice.service.UserService;
import com.oskin.autoservice.utils.MapperToEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    MapperToEntity mapperToEntityMock;

    UserRequest userRequest = new UserRequest("test", "password");
    User user = new User("test", "password", UserRole.USER);

    @Test
    void createUser_WhenNoFoundUser_ShouldCreateUser() {
        Mockito.when(mapperToEntityMock.mapToUserEntity(userRequest)).thenReturn(user);
        Mockito.when(userRepositoryMock.findByLogin(user.getLogin())).thenReturn(null);
        userService.createUser(userRequest);
        Mockito.verify(userRepositoryMock, Mockito.times(1)).create(user);
    }

    @Test
    void createUser_WhenFoundUser_ShouldThrowUserAlreadyExistsException() {
        Mockito.when(mapperToEntityMock.mapToUserEntity(userRequest)).thenReturn(user);
        Mockito.when(userRepositoryMock.findByLogin(user.getLogin())).thenReturn(user);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }
}
