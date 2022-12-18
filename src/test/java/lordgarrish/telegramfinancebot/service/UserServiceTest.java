package lordgarrish.telegramfinancebot.service;

import lordgarrish.telegramfinancebot.entity.User;
import lordgarrish.telegramfinancebot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("Should save user to database")
    void saveUserTest() {
        User user = new User(42L, "John", "Smith", "test_user");

        boolean isSaved = userService.saveUser(user);
        assertTrue(isSaved);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Should return false when trying to save already saved user")
    void saveUserFailedTest() {
        User user = new User(42L, "John", "Smith", "test_user");

        Mockito.doReturn(true)
                .when(userRepository)
                .existsById(42L);

        boolean isSaved = userService.saveUser(user);
        assertFalse(isSaved);
    }
}