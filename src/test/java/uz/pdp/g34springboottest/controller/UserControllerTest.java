package uz.pdp.g34springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import uz.pdp.g34springboottest.domain.User;
import uz.pdp.g34springboottest.dto.UserRegistrationDto;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterUser() throws Exception {
        final var dto = new UserRegistrationDto("test2", "test2@gmial.com", "test");
        ResultActions result = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        result.andExpect(status().isCreated());
        User user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), User.class);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(dto.username(), user.getUsername());
        assertEquals(dto.email(), user.getEmail());
        assertEquals(dto.password(), user.getPassword());

    }
}