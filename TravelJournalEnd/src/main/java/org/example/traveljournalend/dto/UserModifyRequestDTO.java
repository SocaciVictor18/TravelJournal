package org.example.traveljournalend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserModifyRequestDTO(
        @NotBlank @NotNull String name,
        @NotBlank @NotNull String surname
) {
}
