package br.com.vinicius.vaultcore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull Long payerId,
        @NotNull Long payeeId,
        @NotNull @Positive BigDecimal value
        ) {
}
