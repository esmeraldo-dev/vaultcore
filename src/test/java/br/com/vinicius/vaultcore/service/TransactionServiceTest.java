package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.exception.BusinessException;
import br.com.vinicius.vaultcore.model.Transaction;
import br.com.vinicius.vaultcore.model.User;
import br.com.vinicius.vaultcore.model.Wallet;
import br.com.vinicius.vaultcore.repository.TransactionRepository;
import br.com.vinicius.vaultcore.repository.UserRepository;
import br.com.vinicius.vaultcore.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador não tiver saldo suficiente")
    void transferCase1(){
        User payer = new User();
        payer.setId(1L);
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(new BigDecimal("10.00"));
        payer.setWallet(payerWallet);

        User payee = new User();
        payee.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payer));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.transfer(1L, 2L, new BigDecimal("100.00"));
        });

        assertEquals("Saldo insuficiente na conta", exception.getMessage());

        verify(transactionRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso quando todos os dados forem válidos")
    void transferCase2(){
        User payer = new User();
        payer.setId(1L);
        Wallet payerWallet = new Wallet();
        payerWallet.setBalance(new BigDecimal("1000.00"));
        payer.setWallet(payerWallet);

        User payee = new User();
        payee.setId(2L);
        Wallet payeeWallet = new Wallet();
        payeeWallet.setBalance(new BigDecimal("500.00"));
        payee.setWallet(payeeWallet);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal transferValue = new BigDecimal("100.00");
        Transaction result = transactionService.transfer(1L, 2L, transferValue);

        assertEquals(new BigDecimal("900.00"), payer.getWallet().getBalance());
        assertEquals(new BigDecimal("600.00"), payee.getWallet().getBalance());

        verify(transactionRepository, times(1)).save(any());

        assertEquals(transferValue, result.getAmount());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador tenta transferir para si mesmo")
    void transferCase3(){
        Long sameId = 1L;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
           transactionService.transfer(sameId, sameId, new BigDecimal("100.00"));
        });

        assertEquals("Você não pode transferir dinheiro para propria conta!", exception.getMessage());

        verify(userRepository, times(0)).findById(any());
    }
    @Test
    @DisplayName("Deve lançar exceção quando um dos usuários não for encontrado.")
    void transferCase4(){
        Long invalidId = 999L;

        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.transfer(invalidId, 2L, new BigDecimal("100.00"));
        });
        assertEquals("Pagador não encontrado", exception.getMessage());
    }
}