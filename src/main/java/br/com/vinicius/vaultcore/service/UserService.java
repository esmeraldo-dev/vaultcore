package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.dto.UserDTO;
import br.com.vinicius.vaultcore.exception.BusinessException;
import br.com.vinicius.vaultcore.model.User;
import br.com.vinicius.vaultcore.model.Wallet;
import br.com.vinicius.vaultcore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserDTO data) {
        if (userRepository.findByDocument(data.document()).isPresent()) {
            throw new BusinessException("CPF já cadastrado no sistema.");
        }

        User newUser = new User();
        newUser.setFirstName(data.firstName());
        newUser.setLastName(data.lastName());
        newUser.setDocument(data.document());
        newUser.setEmail(data.email());
        newUser.setUserType(data.userType());

        String encryptedPassword = passwordEncoder.encode(data.password());

        newUser.setPassword(encryptedPassword);

        Wallet wallet = new Wallet();
        wallet.setBalance(data.balance());
        wallet.setUser(newUser);
        newUser.setWallet(wallet);

        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
