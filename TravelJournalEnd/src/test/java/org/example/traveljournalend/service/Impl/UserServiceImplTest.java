package org.example.traveljournalend.service.Impl;

import org.example.traveljournalend.dto.UserLoginRequestDTO;
import org.example.traveljournalend.dto.UserModifyRequestDTO;
import org.example.traveljournalend.dto.UserRequestDTO;
import org.example.traveljournalend.dto.UserResponseDTO;
import org.example.traveljournalend.exception.EmailAlreadyExistsException;
import org.example.traveljournalend.exception.InvalidCredentialsException;
import org.example.traveljournalend.exception.ResourceNotFoundException;
import org.example.traveljournalend.mapper.UserMapper;
import org.example.traveljournalend.model.User;
import org.example.traveljournalend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "John", "Doe", "john.doe@example.com", "password");
    }

    @Test
    void getAllUsers_returnsListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_whenUserExists_returnsUserResponseDTO() {
        UserResponseDTO responseDTO = new UserResponseDTO("John", "Doe", "john.doe@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getUserById(1);

        assertEquals("John", result.name());
        assertEquals("Doe", result.surname());
        assertEquals("john.doe@example.com", result.email());
        verify(userRepository, times(1)).findById(1);
        verify(userMapper, times(1)).userToUserResponseDTO(user);
    }

    @Test
    void getUserById_whenUserDoesNotExist_throwsResourceNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void createUser_whenEmailDoesNotExist_savesAndReturnsUserResponseDTO() {
        UserRequestDTO requestDTO = new UserRequestDTO("John", "Doe", "john.doe@example.com", "password");
        UserResponseDTO responseDTO = new UserResponseDTO("John", "Doe", "john.doe@example.com");

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(false);
        when(userMapper.userToUser(requestDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertEquals("John", result.name());
        assertEquals("Doe", result.surname());
        assertEquals("john.doe@example.com", result.email());
        verify(userRepository, times(1)).existsByEmail(requestDTO.email());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).userToUser(requestDTO);
        verify(userMapper, times(1)).userToUserResponseDTO(user);
    }

    @Test
    void createUser_whenEmailExists_throwsEmailAlreadyExistsException() {
        UserRequestDTO requestDTO = new UserRequestDTO("John", "Doe", "john.doe@example.com", "password");

        when(userRepository.existsByEmail(requestDTO.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(requestDTO));
        verify(userRepository, times(1)).existsByEmail(requestDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_whenCredentialsAreValid_returnsSuccessMessage() throws InvalidCredentialsException {
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("john.doe@example.com", "password");

        when(userRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.of(user));

        String result = userService.login(loginRequestDTO);

        assertEquals("Login successful", result);
        verify(userRepository, times(1)).findByEmail(loginRequestDTO.email());
    }

    @Test
    void login_whenEmailNotFound_throwsInvalidCredentialsException() {
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("john.doe@example.com", "password");

        when(userRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginRequestDTO));
        verify(userRepository, times(1)).findByEmail(loginRequestDTO.email());
    }

    @Test
    void login_whenPasswordIsIncorrect_throwsInvalidCredentialsException() {
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO("john.doe@example.com", "wrongPassword");

        when(userRepository.findByEmail(loginRequestDTO.email())).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginRequestDTO));
        verify(userRepository, times(1)).findByEmail(loginRequestDTO.email());
    }

    @Test
    void updateUser_whenUserExists_updatesAndReturnsUserResponseDTO() {
        UserModifyRequestDTO modifyRequestDTO = new UserModifyRequestDTO("Jane", "Smith");
        UserResponseDTO responseDTO = new UserResponseDTO("Jane", "Smith", "john.doe@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.userToUserResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.updateUser(1, modifyRequestDTO);

        assertEquals("Jane", result.name());
        assertEquals("Smith", result.surname());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).userToUserResponseDTO(user);
    }

    @Test
    void updateUser_whenUserDoesNotExist_throwsResourceNotFoundException() {
        UserModifyRequestDTO modifyRequestDTO = new UserModifyRequestDTO("Jane", "Smith");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1, modifyRequestDTO));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void deleteUser_whenUserExists_deletesAndReturnsSuccessMessage() {
        when(userRepository.existsById(1)).thenReturn(true);

        String result = userService.deleteUser(1);

        assertEquals("User deleted successfully", result);
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_whenUserDoesNotExist_throwsResourceNotFoundException() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1));
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, never()).deleteById(anyInt());
    }
}
