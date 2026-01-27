package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.exception.BusinessException;
import br.com.vinicius.vaultcore.model.User;
import br.com.vinicius.vaultcore.model.Wallet;
import br.com.vinicius.vaultcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.findByCpf(user.getCpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado no sistema.");
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);

        wallet.setUser(user);

        return userRepository.save(user);
    }
}
