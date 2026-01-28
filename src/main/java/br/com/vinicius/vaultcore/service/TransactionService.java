package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.exception.BusinessException;
import br.com.vinicius.vaultcore.model.Transaction;
import br.com.vinicius.vaultcore.model.User;
import br.com.vinicius.vaultcore.repository.TransactionRepository;
import br.com.vinicius.vaultcore.repository.UserRepository;
import br.com.vinicius.vaultcore.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public Transaction transfer(Long payerId, Long payeeId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da transferência deve ser maios que zero");
        }

        if (payerId.equals(payeeId)) {
            throw new BusinessException("Você não pode transferir dinheiro para propria conta!");
        }

        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new BusinessException("Pagador não encontrado"));

        User payee = userRepository.findById(payeeId)
                .orElseThrow(() -> new BusinessException("Recebedor não encontrado."));

        if (payer.getWallet().getBalance().compareTo(amount) <= 0) {
            throw new BusinessException("Saldo insuficiente na conta");
        }

        payer.getWallet().setBalance(payer.getWallet().getBalance().subtract(amount));

        payee.getWallet().setBalance(payee.getWallet().getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setPayer(payer.getWallet());
        transaction.setPayee(payee.getWallet());
        transaction.setAmount(amount);

        return transactionRepository.save(transaction);
    }
}
