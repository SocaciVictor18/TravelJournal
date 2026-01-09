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
import org.example.traveljournalend.service.UserService;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public @Nullable UserResponseDTO getUserById(Integer id) {
        return userMapper
                .userToUserResponseDTO(userRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Override
    @Transactional
    public @Nullable UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = userMapper.userToUser(userRequestDTO);
        return userMapper
                .userToUserResponseDTO(userRepository.save(user));
    }

    @Override
    public String login(UserLoginRequestDTO userLoginRequestDTO) throws InvalidCredentialsException {
        User user = userRepository.findByEmail((userLoginRequestDTO.email()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!userLoginRequestDTO.password().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return "Login successful";
    }

    @Override
    public @Nullable UserResponseDTO updateUser(Integer id, UserModifyRequestDTO userModifyRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(userModifyRequestDTO.name());
        user.setSurname(userModifyRequestDTO.surname());

        userRepository.save(user);

        return userMapper.userToUserResponseDTO(user);
    }

    @Override
    public @Nullable String deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
