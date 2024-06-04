package uz.pdp.g34springboottest.dto;

public record UserRegistrationDto(
        String username,
        String email,
        String password
) {
}