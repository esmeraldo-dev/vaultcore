package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.client.AuthorizationClient;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private AuthorizationClient authClient;
    @Mock
    private NotificationService notificationService;

    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(
                transactionRepository,
                userRepository,
                walletRepository,
                authClient,
                notificationService
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador não tiver saldo suficiente")
    void transferCase1(){
        User payer = createUser(1L, "10.00");
        User payee = createUser(2L, "50.00");

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));
        when(authClient.isAuthorized()).thenReturn(Map.of("status", "success"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.transfer(1L, 2L, new BigDecimal("100.00"));
        });

        assertEquals("Saldo insuficiente na conta", exception.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso quando todos os dados forem válidos")
    void transferCase2(){
        User payer = createUser(1L, "1000.00");
        User payee = createUser(2L, "500.00");

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(payer));
        when(userRepository.findById(eq(2L))).thenReturn(Optional.of(payee));

        Map<String, Object> authResponse = new java.util.HashMap<>();
        authResponse.put("status", "success");
        when(authClient.isAuthorized()).thenReturn(authResponse);
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BigDecimal transferValue = new BigDecimal("100.00");
        Transaction result = transactionService.transfer(1L, 2L, transferValue);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("900.00").compareTo(payer.getWallet().getBalance()));
        assertEquals(0, new BigDecimal("600.00").compareTo(payee.getWallet().getBalance()));
        verify(transactionRepository, times(1)).save(any());
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

    private User createUser(Long id, String balance) {
        User user = new User();
        user.setId(id);
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal(balance));
        user.setWallet(wallet);
        return user;
    }
}