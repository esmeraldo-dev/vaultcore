package br.com.vinicius.vaultcore.dto;

import br.com.vinicius.vaultcore.model.UserType;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserType userType
) {
}
