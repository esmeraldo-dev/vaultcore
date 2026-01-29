package br.com.vinicius.vaultcore.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        Long payerId,
        Long payeeId,
        LocalDateTime timestamp
) {
}
