package org.example.traveljournalend.mapper;

import org.example.traveljournalend.dto.UserRequestDTO;
import org.example.traveljournalend.dto.UserResponseDTO;
import org.example.traveljournalend.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO userToUserResponseDTO(User user);

    List<UserResponseDTO> usersToUserResponseDTOs(List<User> users);

    User userToUser(UserRequestDTO userRequestDTO);
}
