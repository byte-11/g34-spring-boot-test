package uz.pdp.g34springboottest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.g34springboottest.domain.User;
import uz.pdp.g34springboottest.dto.UserRegistrationDto;
import uz.pdp.g34springboottest.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRegistrationDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(dto));
    }
}
