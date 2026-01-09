package org.example.traveljournalend.controller;

import jakarta.validation.Valid;
import org.example.traveljournalend.dto.UserLoginRequestDTO;
import org.example.traveljournalend.dto.UserModifyRequestDTO;
import org.example.traveljournalend.dto.UserRequestDTO;
import org.example.traveljournalend.dto.UserResponseDTO;
import org.example.traveljournalend.exception.InvalidCredentialsException;
import org.example.traveljournalend.mapper.UserMapper;
import org.example.traveljournalend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travel-journal")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::userToUserResponseDTO)
                .toList();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .body(userService.getUserById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) throws InvalidCredentialsException {
        return ResponseEntity
                .ok()
                .body(userService.login(userLoginRequestDTO));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRequestDTO));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody UserModifyRequestDTO userModifyRequestDTO) {
        return ResponseEntity
                .ok()
                .body(userService.updateUser(id, userModifyRequestDTO));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .body(userService.deleteUser(id));
    }

}
