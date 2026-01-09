package org.example.traveljournalend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
         @NotBlank @NotNull String name,
         @NotBlank @NotNull String surname,
         @NotBlank @NotNull String email,
         @NotBlank @NotNull String password
) {
}
