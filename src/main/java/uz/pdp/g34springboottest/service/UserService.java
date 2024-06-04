package uz.pdp.g34springboottest.service;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import uz.pdp.g34springboottest.domain.User;
import uz.pdp.g34springboottest.dto.UserRegistrationDto;
import uz.pdp.g34springboottest.repo.UserRepository;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_EMAIL_VALIDATION;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_PASSWORD_VALIDATION;
import static uz.pdp.g34springboottest.util.ErrorMessages.ERROR_USERNAME_VALIDATION;

@Service
public class UserService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserRegistrationDto dto) {
        if (dto.email() == null || dto.password() == null || dto.username() == null) {
            throw new IllegalArgumentException("email, password, username is required");
        }

        if (dto.username().isBlank() || dto.username().length() < 2) {
            throw new IllegalArgumentException(ERROR_USERNAME_VALIDATION);
        }

        if (!EMAIL_PATTERN.matcher(dto.email()).matches()) {
            throw new IllegalArgumentException(ERROR_EMAIL_VALIDATION);
        }

        if (dto.password().isBlank() || dto.password().length() < 4) {
            throw new IllegalArgumentException(ERROR_PASSWORD_VALIDATION);
        }

        final var user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .build();
        return userRepository.save(user);
    }
}
