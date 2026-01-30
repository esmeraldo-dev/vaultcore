package br.com.vinicius.vaultcore.dto;


import br.com.vinicius.vaultcore.model.UserType;

import java.math.BigDecimal;

public record UserDTO(
        String firstName,
        String lastName,
        String document,
        String email,
        String password,
        BigDecimal balance,
        UserType userType
) {
}
