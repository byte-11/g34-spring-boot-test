package uz.pdp.g34springboottest.service;


import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import uz.pdp.g34springboottest.domain.User;
import uz.pdp.g34springboottest.dto.UserRegistrationDto;
import uz.pdp.g34springboottest.repo.UserRepository;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_EMAIL_VALIDATION;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_PASSWORD_VALIDATION;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_USERNAME_VALIDATION;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @ParameterizedTest
    @MethodSource("userRegistrationDtoStreamForNullChecking")
    void testRegisterThatThrowsExceptionForNullData(UserRegistrationDto dto) {
        assertThrows(IllegalArgumentException.class, () -> userService.register(dto));
    }

    private static Stream<UserRegistrationDto> userRegistrationDtoStreamForNullChecking() {
        return Stream.of(
                new UserRegistrationDto(null, null, null),
                new UserRegistrationDto(EMPTY_STRING, null, null),
                new UserRegistrationDto(EMPTY_STRING, EMPTY_STRING, null),
                new UserRegistrationDto(null, EMPTY_STRING, EMPTY_STRING),
                new UserRegistrationDto(EMPTY_STRING, null, EMPTY_STRING)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {EMPTY_STRING, "p", " ", "  ", "    "})
    void testRegisterThatThrowsExceptionForUsernameValidation(String username) {
        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(new UserRegistrationDto(username, EMPTY_STRING, EMPTY_STRING))
        );
        assertEquals(ERROR_USERNAME_VALIDATION, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "x@m@gom", "x@gail"})
    void testRegisterThatThrowsExceptionForEmailValidation(String email) {
        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(new UserRegistrationDto("pi", email, EMPTY_STRING))
        );
        assertEquals(ERROR_EMAIL_VALIDATION, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "x", "xx", "xxx"})
    void testRegisterThatThrowsExceptionForPasswordValidation(String password) {
        final var exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(new UserRegistrationDto("test", "test@gmail.com", password))
        );
        assertEquals(ERROR_PASSWORD_VALIDATION, exception.getMessage());
    }

    @Test
    void testRegisterThatSuccessfullySavesAndReturnsUser() {
        final var dto = new UserRegistrationDto("test", "test@gmail.com", "test");
        final var user = User.builder().id(1L).username(dto.username()).email(dto.email()).password(dto.password()).build();

        when(userRepository.save(any())).thenReturn(user);

        final var savedUser = userService.register(dto);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(dto.username(), savedUser.getUsername());
        assertEquals(dto.email(), savedUser.getEmail());
        assertEquals(dto.password(), savedUser.getPassword());

        verify(userRepository).save(any());
    }
}