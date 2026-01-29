package br.com.vinicius.vaultcore.controller;

import br.com.vinicius.vaultcore.dto.TransactionDTO;
import br.com.vinicius.vaultcore.dto.TransactionResponseDTO;
import br.com.vinicius.vaultcore.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody @Valid TransactionDTO data) {
        TransactionResponseDTO newTransaction = transactionService.transfer(
                data.payerId(),
                data.payeeId(),
                data.value()
        );

        return ResponseEntity.ok(newTransaction);
    }
}
