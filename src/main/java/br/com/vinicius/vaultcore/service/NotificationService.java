package br.com.vinicius.vaultcore.service;

import br.com.vinicius.vaultcore.client.NotificationClient;
import br.com.vinicius.vaultcore.dto.NotificationDTO;
import br.com.vinicius.vaultcore.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    @Async
    public void sendNotification(User user, String message) {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        try {
            notificationClient.sendNotification(notificationRequest);
            System.out.println("Notificação enviada com sucesso para: " + email);
        } catch (Exception e) {
            System.err.println("ALERTA: Falha ao notificar " + user.getEmail() + " mas a transação foi salva. Erro: " + e.getMessage());
        }
    }

}
