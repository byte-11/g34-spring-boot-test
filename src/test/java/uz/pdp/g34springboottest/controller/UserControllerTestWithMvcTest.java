package uz.pdp.g34springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import uz.pdp.g34springboottest.domain.User;
import uz.pdp.g34springboottest.dto.UserRegistrationDto;
import uz.pdp.g34springboottest.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTestWithMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    void testRegisterThatSuccessfullySavesUser() throws Exception {
        final var dto = new UserRegistrationDto("test", "test@gmial.com", "test");
        final var user = User.builder().username(dto.username()).email(dto.email()).password(dto.password()).build();

        when(userService.register(any())).thenReturn(user);

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));

        verify(userService).register(any());
    }
}