package org.example.traveljournalend.service;

import jakarta.validation.Valid;
import org.example.traveljournalend.dto.UserLoginRequestDTO;
import org.example.traveljournalend.dto.UserModifyRequestDTO;
import org.example.traveljournalend.dto.UserRequestDTO;
import org.example.traveljournalend.dto.UserResponseDTO;
import org.example.traveljournalend.exception.InvalidCredentialsException;
import org.example.traveljournalend.model.User;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface UserService {
    @Nullable List<User> getAllUsers();

    @Nullable UserResponseDTO createUser(@Valid UserRequestDTO userRequestDTO);

    @Nullable UserResponseDTO getUserById(Integer id);

    String login(@Valid UserLoginRequestDTO userLoginRequestDTO) throws InvalidCredentialsException;

    @Nullable UserResponseDTO updateUser(Integer id, @Valid UserModifyRequestDTO userModifyRequestDTO);

    @Nullable String deleteUser(Integer id);
}
