package org.example.traveljournalend.repository;

import org.example.traveljournalend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void existsByEmail_returnsTrueWhenEmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = userRepository.existsByEmail("john.doe@example.com");

        assertTrue(exists);
    }

    @Test
    void findByEmail_returnsUserWhenEmailExists() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }
}
