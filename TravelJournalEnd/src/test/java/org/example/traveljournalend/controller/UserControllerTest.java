package org.example.traveljournalend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.traveljournalend.dto.UserLoginRequestDTO;
import org.example.traveljournalend.dto.UserModifyRequestDTO;
import org.example.traveljournalend.dto.UserRequestDTO;
import org.example.traveljournalend.dto.UserResponseDTO;
import org.example.traveljournalend.exception.InvalidCredentialsException;
import org.example.traveljournalend.mapper.UserMapper;
import org.example.traveljournalend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;


    @Test
    void getAllUsers_returnsListOfUsers() throws Exception {
        List<UserResponseDTO> users = List.of(
                new UserResponseDTO("John", "Doe", "john.doe@example.com"),
                new UserResponseDTO("Jane", "Smith", "jane.smith@example.com")
        );

        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/travel-journal/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_returnsUser() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO("John", "Doe", "john.doe@example.com");

        when(userService.getUserById(1)).thenReturn(responseDTO);

        mockMvc.perform(get("/travel-journal/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void login_whenCredentialsValid_returnsOk() throws Exception, InvalidCredentialsException {
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("john.doe@example.com", "password");

        when(userService.login(any(UserLoginRequestDTO.class))).thenReturn("Login successful");

        mockMvc.perform(post("/travel-journal/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_whenCredentialsInvalid_returnsErrorStatus() throws Exception, InvalidCredentialsException {
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("john.doe@example.com", "wrongPassword");

        when(userService.login(any(UserLoginRequestDTO.class)))
                .thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/travel-journal/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUser_returnsCreatedUser() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("John", "Doe", "john.doe@example.com", "password");
        UserResponseDTO responseDTO = new UserResponseDTO("John", "Doe", "john.doe@example.com");

        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/travel-journal/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void updateUser_returnsUpdatedUser() throws Exception {
        UserModifyRequestDTO modifyRequestDTO = new UserModifyRequestDTO("Jane", "Smith");
        UserResponseDTO responseDTO = new UserResponseDTO("Jane", "Smith", "john.doe@example.com");

        when(userService.updateUser(eq(1), any(UserModifyRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/travel-journal/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.surname").value("Smith"));
    }

    @Test
    void deleteUser_returnsSuccessMessage() throws Exception {
        when(userService.deleteUser(1)).thenReturn("User deleted successfully");

        mockMvc.perform(delete("/travel-journal/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}
